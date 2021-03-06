
package controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.ConferenceService;
import services.PanelService;
import domain.Panel;

@Controller
@RequestMapping("/panel")
public class PanelController extends AbstractController {

	final String					lang	= LocaleContextHolder.getLocale().getLanguage();

	@Autowired
	private PanelService			panelService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConferenceService		conferenceService;

	@Autowired
	private ActorService			actorService;


	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int conferenceId, final String fromConferenceDisplay) {
		ModelAndView result;
		final Panel panel = this.panelService.create();
		result = this.createEditModelAndView(panel, conferenceId);
		result.addObject("conferenceId", conferenceId);
		result.addObject("fromConferenceDisplay", fromConferenceDisplay);
		return result;
	}
	// LIST  ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int conferenceId) {
		ModelAndView result;
		result = new ModelAndView("panel/list");

		final Collection<Panel> panels = this.panelService.findPanelsByConference(conferenceId);
		result.addObject("panels", panels);
		result.addObject("conferenceId", conferenceId);
		final boolean isDraft = this.conferenceService.findOne(conferenceId).getIsDraft();
		result.addObject("isDraft", isDraft);

		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int panelId, @RequestParam final int conferenceId) {

		final ModelAndView result;
		final Panel panel = this.panelService.findOne(panelId);
		result = new ModelAndView("panel/display");
		result.addObject("panel", panel);
		result.addObject("conferenceId", conferenceId);
		final boolean isDraft = this.conferenceService.findOne(conferenceId).getIsDraft();
		result.addObject("isDraft", isDraft);

		return result;
	}

	// UPDATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int panelId, @RequestParam final int conferenceId) {
		ModelAndView result;
		Panel panel;

		panel = this.panelService.findOne(panelId);

		if (panel != null)
			result = this.createEditModelAndView(panel, conferenceId);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Panel panel, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramPanelId;
		Integer conferenceId;

		paramPanelId = request.getParameter("conferenceId");
		conferenceId = paramPanelId.isEmpty() ? null : Integer.parseInt(paramPanelId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(panel, conferenceId);
		else
			try {
				this.panelService.save(panel, conferenceId);
				result = this.list(conferenceId);
			} catch (final Throwable e) {
				result = this.createEditModelAndView(panel, "panel.commit.error", conferenceId);
			}
		return result;
	}

	// DELETE -----------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int conferenceId, @RequestParam final int panelId) {

		ModelAndView res;
		final Panel toDelete = this.panelService.findOne(panelId);

		try {
			this.panelService.delete(toDelete, conferenceId);
			res = this.list(conferenceId);
		} catch (final Throwable oops) {
			res = this.display(panelId, conferenceId);
			final String error = "delete.error";
			res.addObject("error", error);
		}
		return res;
	}

	// CREATEEDITMODELANDVIEW -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Panel panel, final int conferenceId) {
		return this.createEditModelAndView(panel, null, conferenceId);
	}

	protected ModelAndView createEditModelAndView(final Panel panel, final String messageCode, final int conferenceId) {
		final ModelAndView result;

		this.administratorService.findByPrincipal();

		result = new ModelAndView("panel/edit");
		result.addObject("panel", panel);
		result.addObject("message", messageCode);
		result.addObject("actors", this.actorService.findAll());
		result.addObject("conferenceId", conferenceId);

		return result;
	}

}
