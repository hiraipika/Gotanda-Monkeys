package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserShowCustomerController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;


	@RequestMapping(path="user/detail", method=RequestMethod.GET)
	public String userDetail(Model model) {
		
		Integer id = ((UserBean)session.getAttribute("user")).getId();
		
		User user = userRepository.getById(id);
		
		model.addAttribute("user", user);
		
		return "user/detail/user_detail";
	}
	
	@RequestMapping(path="user/detail/{id}", method=RequestMethod.POST)
	public String userDetailBack(@PathVariable int id, Model model) {
		
		User user = userRepository.getById(id);
		
		model.addAttribute("user", user);
		
		return "user/detail/user_detail";	
		
	}
	
}