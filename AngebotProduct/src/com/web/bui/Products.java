package com.web.bui;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Products")
public class Products {
	private List<Product> productList;

	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getProductlist(){
		productList=new ArrayList<>();
		productList.add(new Product("Iphone10", "Apple", 700, "http://static1.uk.businessinsider.com/image/56a24731c08a80431d8b90d5-960/iphone-7-concept-curved-display.jpg"));
		productList.add(new Product("Samsung10", "Samsung", 600, "http://static1.uk.businessinsider.com/image/56a24731c08a80431d8b90d5-960/iphone-7-concept-curved-display.jpg"));
		productList.add(new Product("HTC10", "HTC", 500, "http://www.chip.de/ii/4/3/9/7/7/6/6/3/LGG5800x800.png-1ce15a5f54e3b88c.jpg"));
		productList.add(new Product("LG10", "LG", 650, "http://www.chip.de/ii/4/3/9/7/7/6/6/3/LGG5800x800.png-1ce15a5f54e3b88c.jpg"));
		productList.add(new Product("Nexus10", "Google", 450, "http://cdn2.gsmarena.com/vv/pics/huawei/huawei-nexus-6p-1.jpg"));
		productList.add(new Product("Huawei10", "Huawei", 400, "http://www.gizok.com/media/catalog/product/cache/1/image/350x/9df78eab33525d08d6e5fb8d27136e95/h/u/huawei_p9_3_6.jpg"));
		return productList;
	}
	

	
	
}
