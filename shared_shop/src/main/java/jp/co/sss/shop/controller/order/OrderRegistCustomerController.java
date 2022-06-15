package jp.co.sss.shop.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
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
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	// お届け先入力画面表示
	@RequestMapping(path = "/address/input")
	public String addressInput(boolean backFlg, Model model, @ModelAttribute OrderForm form) {
		// 戻るボタンかどうかを判定

		if (!backFlg) {
			// 入力対象の会員情報を取得
			Integer userId = ((UserBean) session.getAttribute("user")).getId();
			User user = userRepository.getById(userId);

			// お届け先入力欄に初期値を設定する処理
			OrderBean orderBean = new OrderBean();
			OrderForm orderForm = new OrderForm();
			// Userエンティティの各フィールドの値をorderBeanにコピー
			BeanUtils.copyProperties(user, orderBean);
			// OrderBeanエンティティの各フィールドの値をorderFormにコピー
			BeanUtils.copyProperties(orderBean, orderForm);

			// 会員情報をViewに渡す
			model.addAttribute("orderForm", orderForm);
			model.addAttribute("user", user);

		} else {

			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);
			// 会員情報をViewに渡す
			model.addAttribute("order", userBean);
		}
		return "order/regist/order_address_input";
	}

	// 支払い方法画面の表示
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String inputPayment(@Valid @ModelAttribute OrderForm form, BindingResult result, Model model) {

		// お届け先入力でエラーがあったらエラーメッセージを表示
		if (result.hasErrors()) {
			return "order/regist/order_address_input";
		}

		OrderBean orderBean = new OrderBean();
		User user = new User();
		// 入力値を会員情報にコピー
		BeanUtils.copyProperties(form, orderBean);

		// 会員情報をViewに渡す
		model.addAttribute("order", orderBean);
		model.addAttribute("user", user);

		return "order/regist/order_payment_input";
	}

	// 注文最終確認画面
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder(@ModelAttribute OrderForm form, Model model) {
		// UserエンティティとOrderBeanのオブジェクトを作成
		User user = new User();
		OrderBean orderBean = new OrderBean();

		// 入力値を会員情報にコピー
		BeanUtils.copyProperties(form, orderBean);

		// 会員情報をViewに渡す

		model.addAttribute("user", user);

		List<OrderItemBean> orderItemBean = new ArrayList<>();
		List<BasketBean> items = (ArrayList) session.getAttribute("orderItem");
		Integer total = 0;

		for (BasketBean item : items) {
			OrderItemBean orderitembean = new OrderItemBean();

			BeanUtils.copyProperties(item, orderitembean);

			// 商品情報データベースから必要な情報をorderitembeanにセットする
			Item itemEntity = itemRepository.getById(item.getId());
			orderitembean.setImage(itemEntity.getImage());
			orderitembean.setPrice(itemEntity.getPrice());
			orderitembean.setSubtotal(itemEntity.getPrice() * item.getOrderNum());

			total += orderitembean.getSubtotal();

			orderItemBean.add(orderitembean);

		}
		orderBean.setTotal(total);
		
		model.addAttribute("order", orderBean);
		session.setAttribute("orderItems", orderItemBean);
		System.out.println(orderBean.getTotal());
		return "order/regist/order_check";
	}

	// 注文確定画面
	@RequestMapping(path = "/order/complete", method = RequestMethod.POST)
	public String completeOrder(OrderForm form) {

		// ログインしたユーザーの会員番号を取得
		Order order = new Order();
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		User user = userRepository.getById(userId);
		// フォームで入力した値をorderエンティティにコピー
		BeanUtils.copyProperties(form, order);
		// 会員番号はフォームで入力せずに、ログインしている人のIDを持ってきてセットする
		order.setUser(user);
		// データベースのorderテーブルへの登録
		orderRepository.save(order);

		
		
		// データベースのorderItemテーブルへの登録
		List<BasketBean> items = (ArrayList) session.getAttribute("orderItem");
		
		for (BasketBean basket : items) {
			OrderItem orderItem = new OrderItem();
			Item itemEntity = itemRepository.getById(basket.getId());
			orderItem.setQuantity(basket.getOrderNum());
			orderItem.setItem(itemEntity);
			orderItem.setOrder(order);
			orderItem.setPrice(itemEntity.getPrice());

			orderItemRepository.save(orderItem);
		}
		
		//データベースのItemsテーブルのstockからorderItemのquantity分を引く
		/**
		 * 
		 */
		return "order/regist/order_complete";
	}
}
