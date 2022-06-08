package jp.co.sss.shop.controller.login;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.LoginForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * ログイン機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class LoginController {

	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository	userRepository;

	/**
	 * セッション情報
	 */
	@Autowired
	HttpSession	session;

	/**
	 * ログイン処理
	 *
	 * @param form ログインフォーム
	 * @return "login" ログイン画面へ
	 */
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute LoginForm form) {

		// セッション情報を無効にする
		session.invalidate();

		return "login";
	}

	/**
	 * ログイン処理
	 *
	 * @param form ログインフォーム
	 * @param result 入力チェック結果
	 * @param session セッション情報
	 * @return 
			一般会員の場合 "/" トップ画面へ
			運用管理者、システム管理者の場合 "admin_menu"へ
	 */
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String doLogin(@Valid @ModelAttribute LoginForm form, BindingResult result,Model model) {

		if (result.hasErrors()) {
			// 入力値に誤りがあった場合
			return login(form);
		} else {
			Integer authority = ((UserBean) session.getAttribute("user")).getAuthority();
			if (authority.intValue() == 2) {
				User user = userRepository.findByEmail(form.getEmail());
				model.addAttribute("user", user);
				return "user/detail/user_detail";
			}
			else {
				// 運用管理者、もしくはシステム管理者としてログインした場合、管理者用メニュー画面に遷移
				return "admin_menu";
			}
		}
	}
}
