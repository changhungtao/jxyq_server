package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.ConContext;
import com.jxyq.model.QA;
import com.jxyq.model.consultation.HealthConsultation;
import com.jxyq.model.consultation.QuestionAndReply;
import com.jxyq.model.consultation.QuestionAndReplyResource;

import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/3/18.
 */
public interface ConsultationService {
    //  获取专家团队
    List<Map<String, Object>> getExpertTeams();

    //  获取专家号码
    String getExpertPhone(int id);

    Integer getUserPoints(long userId);

    //    更新积分
    void upUserPoints(Integer points, int userId);

    //  获取QA
    QA getQA(int id);

    //  获取咨询内容
    List<ConContext> getContext(Map<String, String> map);

    // 创建咨询内容,返回插入数据库条目的id
    void createConsultation(ConContext conContext);

    //    创建问题
    void createQA(QA qa);

    void inQuestionAndReply(QuestionAndReply qa);

    //    创建问答资源
    void createQAResource(Map<String, Object> map);

    // 增加咨询
    public void addQA(Map<String, Object> map);

    void upConsultation(Map<String, Object> map);

    // 关闭咨询
    void closeContent(Map<String, Object> map);

    List<ConContext> selConsultation(Map<String, Object> map, PagingCriteria pagingCriteria);

    List<Map<String, Object>> selConsultDetail(int health_consultation_id);

    List<Map<String, Object>> selHealthConByPage(Map<String, Object> map);

    HealthConsultation selHealthConById(long cid);

    List<QuestionAndReplyResource> selQuestionAndReplyByConId(long cid);
}
