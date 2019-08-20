
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Integer> {

	@Query("select distinct p from Conference p join p.category c where (?1='' OR p.acronym LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.venue LIKE CONCAT('%',?1,'%') OR p.summary LIKE CONCAT('%',?1,'%')) AND (?2='' OR c.titleEn LIKE CONCAT('%',?2,'%') OR c.titleEs LIKE CONCAT('%',?2,'%')) AND ((p.fee<=?5) OR ?5=NULL) AND ((p.endDate>=?3) OR ?3=NULL) AND ((p.startDate<=?4) OR ?4=NULL)")
	Collection<Conference> findConferences(String keyword, String categoryName, Date fromDate, Date toDate, Double maximumFee);

	@Query("select c from Conference c where c.startDate > CURRENT_TIMESTAMP")
	Collection<Conference> findFurthcomingConferences();

	@Query("select c from Conference c where c.endDate < CURRENT_TIMESTAMP")
	Collection<Conference> findPastConferences();

	@Query("select c from Conference c where c.endDate > CURRENT_TIMESTAMP and c.startDate < CURRENT_TIMESTAMP")
	Collection<Conference> findRunningConferences();

	@Query("select distinct p from Conference p join p.category c where (?1='' OR p.acronym LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.venue LIKE CONCAT('%',?1,'%') OR p.summary LIKE CONCAT('%',?1,'%'))")
	Collection<Conference> findConferences(String keyword);

	@Query("select c from Conference c join c.activities ac where ac.id=?1")
	Conference findConference(int activityId);

	@Query("select c from Conference c WHERE ((month(CURRENT_TIMESTAMP) - month(c.submission) = 0 AND day(CURRENT_TIMESTAMP) - day(c.submission) <=5 AND day(CURRENT_TIMESTAMP) - day(c.submission) >=0) OR ((month(CURRENT_TIMESTAMP) - month(c.submission) = 1 OR month(CURRENT_TIMESTAMP) - month(c.submission) = -11 ) AND day(CURRENT_TIMESTAMP) - day(c.submission) <=-25 AND day(CURRENT_TIMESTAMP) - day(c.submission) >=-30)) ")
	Collection<Conference> findFiveDaysFromSubmissionConferences();

	@Query("select c from Conference c WHERE ((month(c.notification) - month(CURRENT_TIMESTAMP) = 0 AND day(c.notification) - day(CURRENT_TIMESTAMP) <=5 AND day(c.notification) - day(CURRENT_TIMESTAMP) >=0) OR ((month(c.notification) - month(CURRENT_TIMESTAMP) = 1 OR month(c.notification) - month(CURRENT_TIMESTAMP) = -11 )AND day(c.notification) - day(CURRENT_TIMESTAMP) <=-25 AND day(c.notification) - day(CURRENT_TIMESTAMP) >=-30)) ")
	Collection<Conference> findFiveDaysForNotificationConferences();

	@Query("select c from Conference c WHERE ((month(c.cameraReady) - month(CURRENT_TIMESTAMP) = 0 AND day(c.cameraReady) - day(CURRENT_TIMESTAMP) <=5 AND day(c.cameraReady) - day(CURRENT_TIMESTAMP) >=0) OR ((month(c.cameraReady) - month(CURRENT_TIMESTAMP) = 1 OR month(c.cameraReady) - month(CURRENT_TIMESTAMP) = -11) AND day(c.cameraReady) - day(CURRENT_TIMESTAMP) <=-25 AND day(c.cameraReady) - day(CURRENT_TIMESTAMP) >=-30)) ")
	Collection<Conference> findFiveDaysForCameraReadyConferences();

	@Query("select c from Conference c WHERE ((month(c.startDate) - month(CURRENT_TIMESTAMP) = 0 AND day(c.startDate) - day(CURRENT_TIMESTAMP) <=5 AND day(c.startDate) - day(CURRENT_TIMESTAMP) >=0) OR ((month(c.startDate) - month(CURRENT_TIMESTAMP) = 1 OR month(c.startDate) - month(CURRENT_TIMESTAMP) = -11) AND day(c.startDate) - day(CURRENT_TIMESTAMP) <=-25 AND day(c.startDate) - day(CURRENT_TIMESTAMP) >=-30)) ")
	Collection<Conference> findFiveDaysForBeginningConferences();

}
