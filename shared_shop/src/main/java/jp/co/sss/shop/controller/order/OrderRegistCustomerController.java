package jp.co.sss.shop.controller.order;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class OrderRegistCustomerController {
	/**
	 * 注文情報(注文商品や届け先、支払い方法など)
	 */
	@Autowired
	OrderRepository orderRepository;
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;
	
	@RequestMapping(path="/address/input", method=RequestMethod.GET)
	public String addressInput() {
		return "order/regist/order_address_input";
	}
	
	@RequestMapping(path="/address/input", method=RequestMethod.POST)
	public String addressInput(@ModelAttribute OrderForm form) {
		//OrderBean orderBean = new OrderBean();
		Order order = new Order();
		
//		orderBean.setPostalCode(form.getPostalCode());
//		orderBean.setAddress(form.getAddress());
//		orderBean.setName(form.getName());
//		orderBean.setPhoneNumber(form.getPhoneNumber());
//		System.out.println(orderBean.getName());
//		orderRepository.save(order);
		
		order.setPostalCode(form.getPostalCode());
		order.setAddress(form.getAddress());
		order.setName(form.getName());
		order.setPhoneNumber(form.getPhoneNumber());
		System.out.println(form.getName());
		orderRepository.save(order);
		
		return "redirect:/payment/input";
//		return "order_payment_input";
	}
	
	@RequestMapping(path="/payment/input", method=RequestMethod.GET)
	public String paymentInput() {
		return "order/regist/order_payment_input";
	}
	@RequestMapping(path="/payment/input", method=RequestMethod.POST)
	public String paymentInput2() {
		return "order/regist/order_payment_input";
	}
	
}
