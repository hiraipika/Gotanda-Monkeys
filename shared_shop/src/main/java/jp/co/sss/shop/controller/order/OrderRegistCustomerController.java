package jp.co.sss.shop.controller.order;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class OrderRegistCustomerController {
	/**
	 * 注文情報(注文商品や届け先、支払い方法など)
	 */
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	UserRepository userRepository;
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;
	
	//住所入力の初期値設定、入力画面表示
	@RequestMapping(path = "/address/input")
	public String addressInput(boolean backFlg, Model model, @ModelAttribute UserForm form) {

		// 戻るボタンかどうかを判定
		if (!backFlg) {

			// 入力対象の会員情報を取得
			Integer userId = ((UserBean)session.getAttribute("user")).getId();
			User user = userRepository.getById(userId);
			OrderBean orderBean = new OrderBean();
			
			// Userエンティティの各フィールドの値をorderBeanにコピー
			BeanUtils.copyProperties(user, orderBean);
			// 会員情報をViewに渡す
			model.addAttribute("order", orderBean);
			System.out.println(orderBean.getAddress());
			
		} else {
			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);
			// 会員情報をViewに渡す
			model.addAttribute("order", userBean);
		}
		return "order/regist/order_address_input";
	}
	
	
	//支払い方法画面の表示
	@RequestMapping(path="/payment/input", method=RequestMethod.POST)
	public String paymentInput(@ModelAttribute OrderForm form, Model model) {
				OrderBean orderBean = new OrderBean();
				// 入力値を会員情報にコピー
				BeanUtils.copyProperties(form, orderBean);
	
				// 会員情報をViewに渡す
				model.addAttribute("order", orderBean);
				System.out.println(orderBean.getAddress());
				System.out.println(orderBean.getName());
				System.out.println(orderBean.getPayMethod());
			return "order/regist/order_payment_input";
	}
	
	
	//注文最終確認画面
	@RequestMapping(path="/order/check", method=RequestMethod.POST)
	public String orderCheck(@ModelAttribute OrderForm form, Model model) {
				
				OrderBean orderBean = new OrderBean();
				// 入力値を会員情報にコピー
				BeanUtils.copyProperties(form, orderBean);
				// 会員情報をViewに渡す
				model.addAttribute("order", orderBean);
			return "order/regist/order_check";
	}
	
	
	//注文確定画面
	@RequestMapping(path="/order/complete", method=RequestMethod.POST)
	public String orderComplete() {
		return "order/regist/order_complete";
	}
	
	

}
