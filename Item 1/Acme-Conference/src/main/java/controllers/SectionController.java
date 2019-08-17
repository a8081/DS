
package controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import services.AdministratorService;
import services.SectionService;
import domain.Section;

@Controller
@RequestMapping("/section")
public class SectionController extends AbstractController {

	final String					lang	= LocaleContextHolder.getLocale().getLanguage();

	@Autowired
	private SectionService			sectionService;

	@Autowired
	private AdministratorService	administratorService;


	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Section section = this.sectionService.create();
		result = this.createEditModelAndView(section);
		return result;
	}
	// LIST  ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int tutorialId) {
		ModelAndView result;
		result = new ModelAndView("section/list");

		final Collection<Section> sections = this.sectionService.findByTutorial(tutorialId);
		result.addObject("sections", sections);
		result.addObject("tutorialId", tutorialId);
		result.addObject("lang", this.lang);

		this.addAdminIfLogged(result);

		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sectionId, @RequestParam final int tutorialId) {

		final ModelAndView result;
		final Section section = this.sectionService.findOne(sectionId);
		result = new ModelAndView("section/display");
		result.addObject("section", section);
		result.addObject("tutorialId", tutorialId);

		this.addAdminIfLogged(result);

		return result;
	}

	// UPDATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tutorialId) {
		final Section section = this.sectionService.create();
		Assert.notNull(section);
		final ModelAndView result = this.createEditModelAndView(section);
		result.addObject("tutorialId", tutorialId);
		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Section section, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramTutorialId;
		Integer tutorialId;

		paramTutorialId = request.getParameter("tutorialId");
		tutorialId = paramTutorialId.isEmpty() ? null : Integer.parseInt(paramTutorialId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(section);
		else
			try {
				this.sectionService.save(section, tutorialId);
				result = this.list(tutorialId);
			} catch (final Throwable e) {
				result = this.createEditModelAndView(section, "section.commit.error");
			}
		return result;
	}
	// CREATEEDITMODELANDVIEW -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Section section) {
		return this.createEditModelAndView(section, null);
	}

	protected ModelAndView createEditModelAndView(final Section section, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("section/edit");
		result.addObject("section", section);
		result.addObject("message", messageCode);

		return result;
	}

	/**
	 * Este metodo comprueba que haya un actor logeado y si dicho actor corresponde con un administrador a�ade al ModelAnView pasado como parametro un objeto "isAdmin" a true para indicar en la vista que es un administrador el que va a acceder a ella.
	 * 
	 * @param result
	 *            ModelAndView a modificar si el actor loggeado es un administrador
	 * @author a8081
	 */
	protected void addAdminIfLogged(final ModelAndView result) {
		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = context.getAuthentication();
		final Object principal = authentication.getPrincipal();

		if (principal != "anonymousUser" && principal != null) {
			final UserAccount user = (UserAccount) principal;
			if (user != null && this.administratorService.findByUserId(user.getId()) != null)
				result.addObject("isAdmin", true);
		}
	}

}
