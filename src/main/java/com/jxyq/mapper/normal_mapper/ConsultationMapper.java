
package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.ConContext;
import com.jxyq.model.QA;
import com.jxyq.model.consultation.HealthConsultation;
import com.jxyq.model.consultation.QuestionAndReply;
import com.jxyq.model.consultation.QuestionAndReplyResource;

import java.util.List;
import java.util.Map;


public interface ConsultationMapper {
    List<Map<String, Object>> selectExpertTeams();

    String selectExpertPhone(int id);

    Integer selUserPoints(long userId);

    void upUserPoints(Map<String, Object> map);

    QA selectQA(int id);

    List<ConContext> selectContext(Map<String, String> map);

    List<ConContext> selConsultationsByPage(Map<String, Object> map);

    int createContext(ConContext conContext);

    void updateQA(Map<String, Object> map);

    void updateConsultContent(Map<String, Object> map);

    int insertQA(QA qa);

    void inQuestionAndReply(QuestionAndReply qa);

    void insertQAResource(Map<String, Object> map);

    List<Map<String, Object>> selConsultDetail(Map<String, Object> map);

    List<Map<String, Object>> selHealthConByPage(Map<String, Object> map);

    HealthConsultation selHealthConById(Map<String, Object> map);

    List<QuestionAndReplyResource> selQuestionAndReply(Map<String, Object> map);

    Map<String, Object> selExpertTeam(Map<String, Object> map);
}
