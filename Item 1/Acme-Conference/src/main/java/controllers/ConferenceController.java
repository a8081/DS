
package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CategoryService;
import services.ConferenceService;
import services.SponsorshipService;
import domain.Actor;
import domain.Conference;
import domain.Sponsorship;

@Controller
@RequestMapping("/conference")
public class ConferenceController extends AbstractController {

	@Autowired
	private ConferenceService	conferenceService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private CategoryService		categoryService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int conferenceId) {
		ModelAndView result;

		final Conference conference = this.conferenceService.findOne(conferenceId);

		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = context.getAuthentication();
		final Object principal = authentication.getPrincipal();

		Boolean isAuthor = false;
		Boolean availableToSubmit = false;

		final Date now = new Date();

		if (now.before(conference.getSubmission()))
			availableToSubmit = true;

		if (!principal.toString().equals("anonymousUser")) {
			final Actor logged = this.actorService.findByPrincipal();
			final Authority authAuthor = new Authority();
			authAuthor.setAuthority("AUTHOR");
			if (logged.getUserAccount().getAuthorities().contains(authAuthor))
				isAuthor = true;

		}

		String imgBanner = null;
		String targetPage = null;
		final Sponsorship sponsorship = this.sponsorshipService.findRandomSponsorship();
		if (sponsorship != null) {
			imgBanner = sponsorship.getBanner();
			targetPage = sponsorship.getTargetPage();
		}

		if (conference != null && conference.getIsDraft() == false) {
			result = new ModelAndView("conference/display");
			result.addObject("conference", conference);
			result.addObject("imgBanner", imgBanner);
			result.addObject("targetPage", targetPage);
			result.addObject("isAdministrator", false);
			result.addObject("availableToSubmit", availableToSubmit);
			result.addObject("isAuthor", isAuthor);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}
	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		final Collection<Conference> conferences = this.conferenceService.findAllFinal();

		result = new ModelAndView("conference/list");

		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = context.getAuthentication();
		final Object principal = authentication.getPrincipal();

		Boolean isAuthor = false;
		if (!principal.toString().equals("anonymousUser")) {
			final Actor logged = this.actorService.findByPrincipal();
			final Authority authAuthor = new Authority();
			authAuthor.setAuthority("AUTHOR");
			if (logged.getUserAccount().getAuthorities().contains(authAuthor)) {
				isAuthor = true;
				result.addObject("conferenceAvailable", this.conferenceService.conferenceAvailable(logged.getId()));
				result.addObject("conferenceWithRegistration", this.conferenceService.findConferenceWithRegistration(logged.getId()));
			}

		}

		result.addObject("conferences", conferences);
		result.addObject("isAdministrator", false);
		result.addObject("isAuthor", isAuthor);
		result.addObject("requestURI", "conference/list.do");

		return result;
	}

	@RequestMapping(value = "/listFurthcoming", method = RequestMethod.GET)
	public ModelAndView listFurthcoming() {
		final ModelAndView result;

		final Collection<Conference> conferences = this.conferenceService.findFinalFurthcomingConferences();

		result = new ModelAndView("conference/list");
		result.addObject("conferences", conferences);
		result.addObject("isAdministrator", false);
		result.addObject("requestURI", "conference/administrator/listFurthcoming.do");

		return result;
	}

	@RequestMapping(value = "/listPast", method = RequestMethod.GET)
	public ModelAndView listPast() {
		final ModelAndView result;

		final Collection<Conference> conferences = this.conferenceService.findFinalPastConferences();

		result = new ModelAndView("conference/list");
		result.addObject("conferences", conferences);
		result.addObject("isAdministrator", false);
		result.addObject("requestURI", "conference/administrator/listPast.do");

		return result;
	}

	@RequestMapping(value = "/listRunning", method = RequestMethod.GET)
	public ModelAndView listRunning() {
		final ModelAndView result;

		final Collection<Conference> conferences = this.conferenceService.findFinalRunningConferences();

		result = new ModelAndView("conference/list");
		result.addObject("conferences", conferences);
		result.addObject("isAdministrator", false);
		result.addObject("requestURI", "conference/administrator/listRunning.do");

		return result;
	}

	// GROUPEDN BY CATEGORY  ---------------------------------------------------------------

	@RequestMapping(value = "/listCategory", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		result = new ModelAndView("finder/listByCategory");
		result.addObject("categories", this.categoryService.findCategoriesName(this.lang));
		return result;
	}
	@RequestMapping(value = "/listByCategory", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam final String category) {
		ModelAndView result;
		try {
			final Collection<Conference> conferences = this.conferenceService.findConferencesGroupedByCategory(category);
			result = new ModelAndView("finder/listByCategory");
			result.addObject("conferences", conferences);
			result.addObject("categories", this.categoryService.findCategoriesName(this.lang));
			result.addObject("cate", category.toLowerCase());
		} catch (final Throwable e) {
			result = new ModelAndView("finder/listByCategory");
			result.addObject("categories", this.categoryService.findCategoriesName(this.lang));
			result.addObject("errortrace", "commit.error");
		}
		return result;
	}

}
