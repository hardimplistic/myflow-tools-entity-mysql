package cloud.service.tools.entity.mysql.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cloud.service.tools.entity.mysql.service.EntityService;

@RestController
public class EntityController {
	
	@Autowired
	private EntityService entityService;

	// view-source:http://localhost:9999/entity?tableSchema=myflow&tableName=flow_node&packageName=cloud.service.authentication.jpa.entity
	@RequestMapping(value = "/entity")
	public Object entity(
			@RequestParam(value = "tableSchema", required = true) String tableSchema, 
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "packageName", required = false, defaultValue = "") String packageName
			) {
		return entityService.getEntity(tableSchema, tableName, packageName);
	}

	@RequestMapping(value = "/entity/download")
	public void download(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "tableSchema", required = true) String tableSchema, 
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "packageName", required = false, defaultValue = "") String packageName
			) {
		
		Object entity = entityService.getEntity(tableSchema, tableName, packageName);
		
		try {
			
			String clazzName = entityService.getNameCamelCase(tableName, true) + ".java";
			
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(clazzName, "UTF-8"));
			PrintWriter pw = response.getWriter();
			pw.write(entity.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
