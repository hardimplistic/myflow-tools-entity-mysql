package cloud.tools.mysql.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cloud.dependency.core.utils.StringUtils;
import cloud.tools.mysql.utils.ClassBuilder;

@RestController
public class PublicServiceController {

	// view-source:http://localhost:9999/public/service
	@RequestMapping(value = "/public/service")
	public Object entity(
			@RequestParam(value = "directory", required = false, defaultValue = "") String directory
			) {
		
		
		if (StringUtils.isEmpty(directory)) {
			return "";
		}
		
		File dir = new File(directory);
		
		if (!dir.exists()) {
			return "";
		}
		
		ClassBuilder clazz = new ClassBuilder();
		
		clazz.append("package cloud.service;").enter(2);
		clazz.append("public class ServiceContext {").enter();

		File[] list = dir.listFiles();
		for (File serviceFile : list) {
			if (serviceFile.isFile()) {
				continue;
			}
			
			if (serviceFile.getName().endsWith("-ui")) {
				continue;
			}
			
			if (!(serviceFile.getName().indexOf("-service-") > -1 || serviceFile.getName().indexOf("-mqtt-") > -1)) {
				continue;
			}
			
			String serviceName= null;
			
			serviceName = getServiceName(new File(serviceFile, "\\src\\main\\resources\\bootstrap.properties"));
			if (StringUtils.isEmpty(serviceName)) {
				serviceName = getServiceName(new File(serviceFile, "\\src\\main\\resources\\application.properties"));
				if (StringUtils.isEmpty(serviceName)) {
					continue;
				}
			}
			
			//System.out.println("ServiceName: " + serviceName);
			clazz.enter();
			clazz.append(String.format("\t//%s", serviceName)).enter();
			List<String> apis = getServiceApi(serviceName, serviceFile);
			for (String api : apis) {
				//clazz.append("	public static final String MYFLOW_SERVICE_CMS_TOKEN_BIND = \"http://myflow-service-cms/token/bind\";").enter();
				clazz.append(String.format("\t%s", api)).enter();
			}
		}
		
		clazz.enter();
		clazz.append("}").enter();
		
		return clazz.toString();
	}
	
	private String getServiceName(File propertiesFile) {
		
		if (!propertiesFile.exists()) {
			return null;
		}
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFile));
			if (properties.containsKey("spring.application.name")) {
				String value = properties.getProperty("spring.application.name");
				return value;
			}
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<String> getServiceApi(String serviceName, File serviceDir) {
		
		List<String> apis = new LinkedList<>();

		List<String> javaFiles = new LinkedList<>();
		
		getJavaFiles(serviceDir, javaFiles);
		
		Pattern pattern = Pattern.compile("\"(.+)\"");
		
		for (String javafile : javaFiles) {
			if (!javafile.endsWith("Controller.java")) {
				continue;
			}
			List<String> lines = getJavaFileLines(javafile);
			for (String line : lines) {
				if (line.indexOf("@RequestMapping") == -1) {
					continue;
				}
				// System.out.println(line);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					String g = m.group(0);
					String api = g.replaceAll("\"", "");
					String serviceUri = getServiceUri(serviceName, api);
					//System.out.println("\t" + serviceUri);
					apis.add(serviceUri);
					continue;
				}
			}
		}
		
		return apis;
	}
	
	private List<String> getJavaFiles(File file, List<String> resultFileName) {
		File[] files = file.listFiles();
		if (files == null)
			return resultFileName;
		for (File f : files) {
			if (f.isDirectory()) {
				getJavaFiles(f, resultFileName);
			} else {
				if (f.getName().endsWith(".java")) {
					resultFileName.add(f.getPath());
				}
			}
		}
		return resultFileName;
	}
	
	private List<String> getJavaFileLines(String javafile) {
		
		File file = new File(javafile);
		
		List<String> lines = new LinkedList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (StringUtils.isEmpty(line)) {
					continue;
				}
				lines.add(line.trim());
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}

	private String getServiceUri(String serviceName, String api) {
		String serviceUri = serviceName + (api.startsWith("/") ? api : ("/" + api));
		String location = serviceUri.toUpperCase().replaceAll("-", "_").replaceAll("/", "_");
		return String.format("public static final String %s = \"http://%s\";", location, serviceUri);
	}
}
