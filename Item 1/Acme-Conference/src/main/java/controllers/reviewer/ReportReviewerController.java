
package controllers.reviewer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ReportService;
import controllers.AbstractController;
import domain.Report;

@Controller
@RequestMapping("/report/reviewer")
public class ReportReviewerController extends AbstractController {

	@Autowired
	private ReportService	reportService;
	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------
	public ReportReviewerController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		result = new ModelAndView("report/list");
		result.addObject("reports", this.reportService.findReportsByReviewer(this.actorService.findByPrincipal().getId()));
		return result;
	}

	@RequestMapping(value = "/listFinal", method = RequestMethod.GET)
	public ModelAndView listFinal() {
		ModelAndView result;
		result = new ModelAndView("report/list");
		result.addObject("reports", this.reportService.findFinalReportsByReviewer(this.actorService.findByPrincipal().getId()));
		return result;
	}

	@RequestMapping(value = "/listDraft", method = RequestMethod.GET)
	public ModelAndView listDraft() {
		ModelAndView result;
		result = new ModelAndView("report/list");
		result.addObject("reports", this.reportService.findDraftReportsByReviewer(this.actorService.findByPrincipal().getId()));
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid final Report report, final BindingResult binding) {
		ModelAndView result;
		if (!binding.hasErrors()) {
			this.reportService.save(report, report.getSubmission(), report.getReviewer());
			result = new ModelAndView("redirect:/report/reviewer/list.do");
		} else {
			result = new ModelAndView("report/edit");
			result.addObject("report", report);
			result.addObject("errors", binding.getAllErrors());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reportId) {
		ModelAndView result;
		result = new ModelAndView("report/edit");
		final Report report = this.reportService.findOne(reportId);
		Assert.isTrue(report.getIsDraft());
		result.addObject("report", report);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int reportId) {
		ModelAndView result;
		result = new ModelAndView("report/display");
		result.addObject("report", this.reportService.findOne(reportId));
		return result;
	}

}
