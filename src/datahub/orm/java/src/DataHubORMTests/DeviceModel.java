package DataHubORMTests;

import Annotations.column;
import Annotations.column.Index;
import Annotations.column.AssociationType;
import Annotations.table;
import DataHubORM.Model;

@table(name="device")
public class DeviceModel extends Model<DeviceModel>{

	@column(name="code")
	public String code;
	
	//need to specify foreign key name that matches the foreign key in the corresponding belongs to relationship
	@column(name="test_id",Index=Index.ForeignKey,RelationType=AssociationType.BelongsTo)
	public TestModel testModel;
}