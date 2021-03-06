
package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import services.AuthorService;
import services.ConfigurationParametersService;
import services.FinderService;
import domain.Author;
import forms.ActorForm;

@Controller
@RequestMapping("/author")
public class AuthorController extends AbstractController {

	@Autowired
	private AuthorService					authorService;

	@Autowired
	private FinderService					finderService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView viewAuthor() {
		final Author author = this.authorService.findByPrincipal();
		ModelAndView result;
		result = new ModelAndView("author/display");
		result.addObject("author", author);
		result.addObject("requestURI", "author/display.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAuthor() {
		final Author author = this.authorService.findByPrincipal();
		final ActorForm actorForm = new ActorForm();
		actorForm.setAddress(author.getAddress());
		actorForm.setEmail(author.getEmail());
		actorForm.setId(author.getId());
		actorForm.setMiddleName(author.getMiddleName());
		actorForm.setName(author.getName());
		actorForm.setPhone(author.getPhone());
		actorForm.setPhoto(author.getPhoto());
		actorForm.setSurname(author.getSurname());
		actorForm.setUserAccountpassword(author.getUserAccount().getPassword());
		actorForm.setUserAccountuser(author.getUserAccount().getUsername());
		final ModelAndView result;
		result = new ModelAndView("author/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());

		return result;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signupAuthor() {
		final ModelAndView result;

		final ActorForm actorForm = new ActorForm();

		result = new ModelAndView("author/signup");
		result.addObject("actorForm", actorForm);
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());

		return result;
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAuthor(@ModelAttribute("actorForm") @Valid final ActorForm actorForm, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = (actorForm.getId() == 0) ? new ModelAndView("author/signup") : new ModelAndView("author/edit");
			result.addObject("errors", binding.getAllErrors());
			result.addObject("actorForm", actorForm);
			result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());

		} else
			try {
				if (actorForm.getId() != 0) {
					final UserAccount userAccount = this.authorService.findOne((actorForm.getId())).getUserAccount();
					actorForm.setUserAccountuser(userAccount.getUsername());
					actorForm.setUserAccountpassword(userAccount.getPassword());
				}

				final Author author = this.authorService.reconstruct(actorForm, binding);

				this.authorService.save(author);
				if (actorForm.getId() != 0)
					result = this.viewAuthor();
				else
					result = new ModelAndView("forward:/security/login.do");
			} catch (final ValidationException oops) {
				result = (actorForm.getId() == 0) ? new ModelAndView("author/signup") : new ModelAndView("author/edit");
				result.addObject("actorForm", actorForm);
				result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());

			} catch (final Throwable e) {
				result = (actorForm.getId() == 0) ? new ModelAndView("author/signup") : new ModelAndView("author/edit");
				String error = "commit.error";
				if (e.getMessage().contains(".error"))
					error = e.getMessage();
				result.addObject("message", error);
				result.addObject("actorForm", actorForm);
				result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());

			}

		return result;
	}
}
