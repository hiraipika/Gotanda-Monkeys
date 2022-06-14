package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class BasketCustomerController {

	@Autowired
	ItemRepository repository;

	@Autowired
	HttpSession session;

	//商品追加

	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(Integer id, Model model) {

		Integer listId = id-1;
		//買い物カゴに商品があるか確認。
		//ない場合は「リスト」の作成。ある場合は、既存のリストの呼び出し。
		if(session.getAttribute("orderItem")==null) {


			List<BasketBean>items = new ArrayList<>();
			Item item = repository.getById(id);

			//「BasketBean」に代入するための変数宣言

			  String name = item.getName();
			  Integer stock = item.getStock();
			  Integer orderNum = 1;


			//「BasketBean」に代入
			BasketBean bean = new BasketBean(id,name,stock,orderNum);
			items.add(bean);


			//スコープに挿入。
			session.setAttribute("orderItem", items);
			session.setAttribute("items", bean);
			System.out.println(bean.getName());
			System.out.println(bean.getStock());
			System.out.println(bean.getOrderNum());
			System.out.println(bean.getId());
			System.out.println(items.get(0));

			return "basket/basket_shopping";


		} else {
			//ある場合は、既存のリストを呼び出し。
			List<BasketBean> items = (ArrayList)session.getAttribute("orderItem");


			//アイテムが既にリストにあるか確認する。
			//ある場合は上書き。ない場合は新たに作る。
			if(items.get(listId)==null) {

				Item item = repository.getById(id);


				//「BasketBean」に代入するための変数宣言
				String name = item.getName();
				Integer stock = item.getStock();
				Integer orderNum = 1;

				//「BasketBean」に代入
				BasketBean bean = new BasketBean(id,name,stock,orderNum);

				//「List」に挿入。挿入位置を指定する。
				items.add(bean);

				//スコープに挿入。
				session.setAttribute("orderItem", items);
				session.setAttribute("items", bean);


				return "basket/basket_shopping";

			} else {

				BasketBean bean = items.get(listId);

				Item item = repository.getById(id);

				//「BasketBean」に代入するための変数宣言

				Integer newOrderNum = bean.getOrderNum()+1;

				//「BasketBean」に代入
				bean.setOrderNum(newOrderNum);

				//「List」に挿入。挿入位置を指定する。
				items.add(bean);

				//スコープに挿入。
				session.setAttribute("orderItem", items);
				session.setAttribute("items", bean);


				return "basket/basket_shopping";

			}
		}
	}


			public String addItem(Integer id) {
				List<BasketBean>items = new ArrayList<>();
				Item item = repository.getById(id);

				//「BasketBean」に代入するための変数宣言

				String name = item.getName();
				Integer stock = item.getStock();
				Integer orderNum = 1;

				//「BasketBean」に代入
				BasketBean bean = new BasketBean(id,name,stock,orderNum);
				//「List」に挿入。挿入位置を指定する。
				items.add(bean);

				session.setAttribute("orderItem", items);
				session.setAttribute("items", item);
				return "basket/basket_shopping";

			}
}


			/*public BasketBean elicitItemBean (List<BasketBean>items,String itemName) {
				for(BasketBean bean : items) {
					if(bean.getName()==itemName) {
						return bean;
					} else {
					BasketBean bean = new BasketBean(id,name,stock,orderNum);
					return bean;
				}
				}
			}

			/*@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
			public String deleteItem(HttpSession session, BasketBean bean) {
				session.setAttribute("basket",bean.getId());
				return "foward:/basket/list";
				}*/

			/*//買い物かごを空にするボタン
			@RequestMapping(path = "/basket/allDelete", method = RequestMethod.POST)
			public String deleteAll(HttpSession session,BasketBean items) {
				session.setAttribute("basket",items);
				return "foward:/basket/list";
			}*/

			//ご注文お手続きボタン
				/*	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
							public String Inputbutton(BasketBean bean) {
								session.setAttribute("basket",bean) {
								return "order/order_address_input";
						}

}
				/* else {

					List<BasketBean>items = ((List<BasketBean>)session.getAttribute("orderItem"));

				買い物カゴに入れた商品の情報。


				リストの中に当該商品があるかどうかの確認。
				if(items.add(bean.getId()), bean) {
					items.add(bean.getId(), bean);

				Item item = repository.getById(id);

				Integer id2 = item.getId();
				String name = item.getName();
				Integer stock = item.getStock();
				Integer orderNum = 1;

				買い物カゴの内容をビーンに登録
				BasketBean bean = new BasketBean(id2, name, stock, orderNum);





				return "/basket/basket_shopping";
				}
			}
}*/

