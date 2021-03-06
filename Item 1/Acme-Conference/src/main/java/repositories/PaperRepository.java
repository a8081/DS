
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Paper;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Integer> {

	@Query("select s.cameraReadyPaper from Submission s where s.author.userAccount.id=?1 and s.cameraReadyPaper!=null")
	Collection<Paper> findByAuthorUAId(int authorUAId);

	@Query("select s.cameraReadyPaper from Submission s where s.conference.id=?1 and s.cameraReadyPaper!=null and 1.0=1.0*(select case when r.decision='ACCEPT' then 1.0 else 0.0 end from Report r where r.isDraft=false and r.submission.id=s.id)")
	Collection<Paper> findByConference(int conferenceId);

}
