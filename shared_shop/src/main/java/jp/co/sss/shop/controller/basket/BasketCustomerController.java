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

	
	//商品追加。
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(Integer id, Integer orderNum, Model model) {
		
		//買い物カゴに商品があるか確認
		//ない場合は「リスト」の作成。ある場合は、既存のリストの呼び出し
		if(session.getAttribute("orderItem")==null) {
			
			//リストを作成する。
			List<BasketBean>items = new ArrayList<>();
			
			//「買い物カゴ」にアイテムを追加し、データベースの在庫の数を実現する。
			BasketBean bean = stockChangerWithListAdding(id, orderNum);
			
			//リストに挿入する
			items.add(bean);
			
			//スコープに挿入
			session.setAttribute("orderItem", items);
			System.out.println(items.get(0));
			return "basket/basket_shopping";

		} else {
			
			//ある場合は、既存のリストを呼び出し。
			List<BasketBean> items = (ArrayList)session.getAttribute("orderItem");

			//アイテムが既にリストにあるか確認する。
			//ある場合は上書き。ない場合は新たに作りリストに挿入。
			items = elicitItemBean(items,id,orderNum);
			
			//在庫数のチェック
			boolean checkStock = checkIfStockIsOk(items,id);
			
			//スコープに挿入。
			session.setAttribute("orderItem", items);
			model.addAttribute("checkStock", checkStock);
			
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
				
				if(bean.getOrderNum()==0) {
					
					items.remove(number);
					
					boolean checkStock = checkIfStockIsOk(items,id);
					
					model.addAttribute("checkStock", checkStock);
					
					return "basket/basket_shopping";
					
				} 
				
				items.set(number,bean);
				
				boolean checkStock = checkIfStockIsOk(items,id);
				
				model.addAttribute("checkStock", checkStock);
				
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
	
	
	public boolean checkIfStockIsOk (List<BasketBean>items, Integer id) {
		
		Item item = repository.getById(id);
		
			for(BasketBean bean : items) {
				
				if(bean.getId()==id) {
					
					if(bean.getOrderNum()>=item.getStock()) {

						return true;
						
					} else {
						
						return false;
						
					}
					
				}
				
			}
			
			//ここまでは到達しない。
			return false;
			
	}
	
	
	public List<BasketBean> elicitItemBean (List<BasketBean>items, Integer id, Integer orderNum) {
		  
		  //リスト内での要素の位置を調べるための変数。
			int number=0;
			for(BasketBean bean : items) {
			
			//リストにカゴに入れたアイテムが存在した場合
			if(bean.getId()==id) {
				
				bean=stockChanger(bean,id,orderNum);
			
				//位置を指定し「上書き」
				items.set(number,bean);
				
				return items;
	
			} 
			number++;
		}
				
				BasketBean bean=stockChangerWithListAdding(id,orderNum);
						
				items.add(bean);
				
			return items;
			
	}
	
	public BasketBean stockChanger(BasketBean bean,Integer id, Integer orderNum) {
		
			Item item = repository.getById(id);
			
			bean.setOrderNum(bean.getOrderNum()+orderNum);
			
			//bean.setStock(bean.getStock()-orderNum);
			
			item.setStock(item.getStock()-orderNum);
			
			repository.save(item);
			
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
