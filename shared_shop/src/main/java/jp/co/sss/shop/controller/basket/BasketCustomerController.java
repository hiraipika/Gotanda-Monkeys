
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


	@RequestMapping(path = "/basket/list")
	public String basketList() {

		  return "basket/basket_shopping";

	  }


	//商品追加。
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(Integer id, Integer orderNum, Model model) {

		Item item = repository.getById(id);
		List<String>nones = new ArrayList<>();


		//買い物カゴに商品があるか確認
		//ない場合は「リスト」の作成。ある場合は、既存のリストの呼び出し
		if(session.getAttribute("orderItem")==null) {

			//リストを作成する。
			List<BasketBean>items = new ArrayList<>();

			//「買い物カゴ」にアイテムを追加し、データベースの在庫の数を実現する。
			BasketBean bean = stockChangerWithListAdding(id, orderNum);

			//在庫数を最新の値に更新
			bean.setStock(item.getStock());

			//リストに挿入する
			items.add(bean);
			//Collections.sort(items, Collections.reverseOrder());
			//Collections.reverse(items);
			//List descList = items.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());


			for(int i = items.size()-1; i >= 0; --i) {
				Item itemEntity = repository.getById(items.get(i).getId());
				  if(itemEntity.getStock() == 0) {
					  nones.add(itemEntity.getName());
					  items.remove(i);
				   }
			}

			//スコープに
			session.setAttribute("orderItem", items);
			session.setAttribute("nones",nones);

			return "basket/basket_shopping";




		} else {

			//ある場合は、既存のリストを呼び出し。
			List<BasketBean> items = (ArrayList)session.getAttribute("orderItem");

			//アイテムが既にリストにあるか確認する。
			//ある場合は上書き。ない場合は新たに作りリストに挿入。
			Object box[] = elicitItemBean(items,id,orderNum);

			items = (List<BasketBean>)box[0];

			BasketBean bean2 = (BasketBean)box[1];

			//在庫数を最新の値に更新
			bean2.setStock(item.getStock());

			if(bean2.getOrderNum() > item.getStock()) {

				model.addAttribute("err","在庫数を超えた注文ができません。");

				bean2.setOrderNum(item.getStock());

			}
			for(int i = items.size()-1; i > 0; --i) {
				BasketBean basketbean = items.get(i);
				Item itemEntity = repository.getById(items.get(i).getId());
			    if(itemEntity.getStock() <= 0) {
				  nones.add(itemEntity.getName());
				   items.remove(i);
			    }

			}

			//スコープに挿入。
			session.setAttribute("orderItem", items);
			session.setAttribute("nones",nones);


			//スコープ
			return "basket/basket_shopping";

		}

	}


	@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
	public String elicitItemDelete (Integer id, Model model) {

		  //リスト内での要素の位置を調べるための変数。
			List<BasketBean> items = (ArrayList)session.getAttribute("orderItem");

			int number=0;

			for(BasketBean bean : items) {
			//リストにカゴに入れたアイテムが存在した場合

			if(bean.getId()==id) {

				bean.setOrderNum(bean.getOrderNum()-1);

				if(bean.getOrderNum()<=0) {

					items.remove(number);

					return "basket/basket_shopping";

				}

				items.set(number,bean);

				return "basket/basket_shopping";
			}

			number++;

			}

			return "basket/basket_shopping";
	  }


	@RequestMapping(path="/basket/allDelete", method=RequestMethod.GET)
	public String deleteAll() {

		session.removeAttribute("orderItem");

		return"basket/basket_shopping";
	}


	public Object[] elicitItemBean (List<BasketBean>items, Integer id, Integer orderNum) {

		  //リスト内での要素の位置を調べるための変数。
			Object[] box = new Object[2];

			int number=0;

			for(BasketBean bean : items) {

			//リストにカゴに入れたアイテムが存在した場合
			if(bean.getId()==id) {

				bean=stockChanger(bean,id,orderNum);

				//位置を指定し「上書き」
				items.set(number,bean);

				box[0] = items;

				box[1] = bean;

				return box;

			}

			number++;

		}

				BasketBean bean=stockChangerWithListAdding(id,orderNum);

				items.add(bean);

				box[0] = items;

				box[1] = bean;

				return box;

	}


	public BasketBean stockChanger(BasketBean bean,Integer id, Integer orderNum) {

		Item item= repository.getById(id);

		bean.setOrderNum(bean.getOrderNum()+orderNum);

		return bean;
	}


	public BasketBean stockChangerWithListAdding(Integer id, Integer orderNum) {

		Item item = repository.getById(id);

		String name = item.getName();

		Integer stock = item.getStock();

		BasketBean bean = new BasketBean(id,name,stock,orderNum);

		return bean;

	}

}