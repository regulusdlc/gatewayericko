package com.exercise.ericko;

import com.exercise.ericko.model.Gateway;
import com.exercise.ericko.model.PeripheralDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErickoApplicationTests extends AbstractTest{

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@Test
	public void createGateway() throws Exception{

		String uri = "/gateways";
		Gateway gateway = new Gateway();
		gateway.setSerialNumber("testSerial326");
		gateway.setIpv4("111.111.222.101");
		gateway.setGatewayName("MyGateway3");

		String inputJson = super.mapToJson(gateway);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Gateway createdGateway = super.mapFromJson(content, Gateway.class);
		assertTrue(createdGateway.getSerialNumber().equals("testSerial326"));

	}

	@Test
	public void getAllGateways() throws Exception{
		String uri = "/gateways";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Gateway[] gateways = super.mapFromJson(content, Gateway[].class);
		assertTrue(gateways.length > 0);

	}

	@Test
	public void displaySpecificGatewayDetails() throws Exception{

		String uri = "/gateways";
		Gateway gateway = new Gateway();
		gateway.setSerialNumber("testSerial327");
		gateway.setIpv4("111.111.222.101");
		gateway.setGatewayName("MyGateway4");

		String inputJson = super.mapToJson(gateway);

		//create gateway
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		Gateway gateway1 = super.mapFromJson(content, Gateway.class);

		//display created gateway
		uri = "/gateways/"+gateway1.getId().toString();
		mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		content = mvcResult.getResponse().getContentAsString();
		Gateway gateway2 = super.mapFromJson(content, Gateway.class);
		assertTrue(gateway2.getId() == gateway1.getId());
	}



	/*@Test
	public void updateGateway(){

	}*/

	@Test
	public void deleteGateway() throws Exception {

		String uri = "/gateways";
		Gateway gateway = new Gateway();
		gateway.setSerialNumber("testSerial328");
		gateway.setIpv4("111.111.222.101");
		gateway.setGatewayName("MyGateway8");

		String inputJson = super.mapToJson(gateway);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		Gateway gateway1 = super.mapFromJson(content, Gateway.class);


		uri = "/gateways/"+gateway1.getId().toString();
		mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	//PeripheralDevices

	/*@Test
	public void getAllDevicesByGateway(){

	}

	@Test
	public void displaySpecificDeviceDetails(){

	}*/

	@Test
	public void createDevice() throws Exception{

		String uri = "/gateways/1/devices";
		PeripheralDevice device = new PeripheralDevice();
		device.setStatus(true);
		device.setVendorName("testDevice1");

		String inputJson = super.mapToJson(device);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		PeripheralDevice createdDevice = super.mapFromJson(content, PeripheralDevice.class);
		assertTrue(createdDevice.getVendorName().equals("testDevice1"));

	}

	/*@Test
	public void updateDevice(){

	}*/

	@Test
	public void deleteDevice() throws Exception{
		String uri = "/gateways/1/devices"; //change gateway id
		PeripheralDevice device = new PeripheralDevice();
		device.setStatus(true);
		device.setVendorName("testDevice1");

		String inputJson = super.mapToJson(device);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String content = mvcResult.getResponse().getContentAsString();
		PeripheralDevice createdDevice = super.mapFromJson(content, PeripheralDevice.class);
		uri = "/gateways/5/devices/"+createdDevice.getUid().toString();
		mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	//special scenarios

	@Test
	public void limitDevicePerGateway() throws Exception{

		String uri = "/gateways/1/devices"; //change gateway id
		for (int i = 11; i <= 21; i++){ //try to create 11 devices
			PeripheralDevice device = new PeripheralDevice();
			device.setStatus(true);
			device.setVendorName("testDevice"+i);

			String inputJson = super.mapToJson(device);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String exception = mvcResult.getResolvedException().getMessage();
			System.out.println(exception);

			assertEquals(404, status);
			assertTrue(exception.contains("cannot have more than 10 devices!"));
		}

	}

	@Test
	public void wrongIPAddressFormat() throws Exception{

		String uri = "/gateways";
		Gateway gateway = new Gateway();
		gateway.setSerialNumber("testSerial100");
		gateway.setIpv4("111.111.222.300"); //wrong format
		gateway.setGatewayName("SampleGateway");

		String inputJson = super.mapToJson(gateway);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
		assertTrue(content.contains("must match"));
		assertEquals(400, status);

	}

}
