package DataHubORMTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import datahub.DHException;
import datahub.DataHub;

import DataHubAccount.DataHubAccount;
import DataHubAccount.DataHubClient;
import DataHubAccount.DataHubUser;
import DataHubORM.Database;

public class DataHubClientTests {
	public DataHubAccount test_dha;
	public DataHubClient test_dhc;
	public TestDatabase db;
	@Before
	public void setUp() throws DHException, TException{
		test_dha = new DataHubAccount("dggoeh1", new DataHubUser("dggoeh1@mit.edu","dggoeh1"));
		test_dhc = new DataHubClient(test_dha);
		TestDatabase db = new TestDatabase();
		db.setDataHubAccount(this.test_dha);
		try{
			db.connect();
			this.db = db;
		}catch(Exception e){
			
		}
	}
	
	@After
	public void tearDown(){
		db.disconnect();
	}
	
	@Test
	public void testCreateAndDelete(){
		Random generator = new Random();
		String name = "test"+Math.abs(generator.nextInt());
		String description = "test row";
		TestModel t = new TestModel();
		t.name = name;
		t.description = description;
		t.save();
		assertEquals(t.id!=0,true);
		
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("name", name);
		TestModel t1 = this.db.test.findOne(params);
		assertEquals(t1!=null,true);
		assertEquals(t1.name,t.name);
		assertEquals(t1.description,t.description);
		
		
		t.destroy();
		TestModel t2 = this.db.test.findOne(params);
		assertEquals(t2==null,true);
	}
	@Test
	public void testSave(){
		Random generator = new Random();
		String name = "test"+Math.abs(generator.nextInt());
		String description = "test row";
		TestModel t = new TestModel();
		t.name = name;
		t.description = description;
		t.save();
		assertEquals(t.id!=0,true);
		
		int id = t.id;
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		
		//verify creation
		TestModel t1 = this.db.test.findOne(params);
		assertEquals(t1!=null,true);
		assertEquals(t1.name,t.name);
		assertEquals(t1.description,t.description);
		
		//change stuff
		String newName = "lol";
		String newDescription = "he-llo-o";
		t.name = "lol";
		t.description = "he-llo-o";
		t.save();

		TestModel t2 = this.db.test.findOne(params);
		assertEquals(t2!=null,true);
		assertEquals(t2.name,newName);
		assertEquals(t2.description,newDescription);
		
		t.destroy();
		TestModel t3 = this.db.test.findOne(params);
		assertEquals(t3==null,true);
	}

	@Test
	public void basicTest(){
      TTransport transport = new TSocket(
                 "datahub-experimental.csail.mit.edu", 9000);
      try {
		transport.open();
		
		    
		TProtocol protocol = new TBinaryProtocol(transport);
		DataHub.Client client = new DataHub.Client(protocol);
		
		System.out.println(client.get_version());
		transport.close();
      }catch(Exception e){
    	  
      }
	}
	@Test
	public void testGetSchema(){
		TestDatabase db = new TestDatabase();
		db.setDataHubAccount(this.test_dha);
		try{
			db.connect();
			TestModel.setDatabase(db);
			/*for(int i=0; i<10; i++){
				TestModel t = new TestModel();
				t.name = i+"";
				t.description = i+"s description";
				t.save();
			}*/
			//ArrayList<TestModel> results = db.test.findAll();
			HashMap<String, Object> params = new HashMap<String,Object>();
			params.put("name", "f");
			ArrayList<TestModel> results = db.test.findAll(params);
			for(TestModel m1: results){
				System.out.println("id"+m1.id);
				System.out.println("name"+m1.name);
				System.out.println(m1.description);
				//System.out.println(m1.findAll());
				//System.out.println(m1.generateSQLRep());
				//m1.description = "lol";
				//m1.save();
				//m1.destroy();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
