package cloud.service.tools.entity.mysql.api;

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

}
