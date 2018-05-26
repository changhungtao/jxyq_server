package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.ConsultationMapper;
import com.jxyq.model.ConContext;
import com.jxyq.model.QA;
import com.jxyq.model.consultation.HealthConsultation;
import com.jxyq.model.consultation.QuestionAndReply;
import com.jxyq.model.consultation.QuestionAndReplyResource;
import com.jxyq.service.inf.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/3/18.
 */
@Service
public class ConsultationServiceImpl implements ConsultationService {
    @Autowired
    private ConsultationMapper consultationMapper;

//  获取专家团队
    @Override
    public List<Map<String,Object>>getExpertTeams()
    {
        return consultationMapper.selectExpertTeams();
    }

//  获取专家号码
    @Override
    public String getExpertPhone(int id){
        return consultationMapper.selectExpertPhone(id);
    }
//    获取用户积分
    @Override
    public Integer getUserPoints(long userId){
        return consultationMapper.selUserPoints(userId);
    }
//更新积分
    @Override
   public void upUserPoints(Integer points,int userId){
        Map<String, Object> map=new HashMap<>();
        map.put("points",points);
        map.put("userId",userId);
        consultationMapper.upUserPoints(map);
    }
//    查询历史内容
    @Override
    public QA getQA(int id){
        return consultationMapper.selectQA(id);
    }

//    获取咨询内容列表
    @Override
    public List<ConContext> getContext(Map<String,String> map)
    {List<ConContext> l=consultationMapper.selectContext(map);
        return l;}

//  创建咨询内容
    @Override
    public void createConsultation(ConContext conContext){consultationMapper.createContext(conContext);}

//    创建QA
    @Override
    public void createQA( QA qa){consultationMapper.insertQA(qa);}

    @Override
    public void inQuestionAndReply(QuestionAndReply qa) {
        consultationMapper.inQuestionAndReply(qa);
    }

    //    创建问答资源表
   public void createQAResource( Map<String,Object>map){consultationMapper.insertQAResource(map);
    }

//    添加咨询问题
    public void addQA( Map<String,Object>map){
        consultationMapper.updateQA(map);
    }

 //    关闭咨询
    public void closeContent(Map<String,Object>map){
        consultationMapper.updateConsultContent(map);
    }

    @Override
    public void upConsultation(Map<String, Object> map) {
        consultationMapper.updateConsultContent(map);
    }

    @Override
    public List<ConContext> selConsultation(Map<String, Object> map, PagingCriteria pagingCriteria) {
        map.put(BeanProperty.PAGING, pagingCriteria);
        return consultationMapper.selConsultationsByPage(map);
    }

    @Override
    public List<Map<String, Object>> selConsultDetail(int health_consultation_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("health_consultation_id", health_consultation_id);
        return consultationMapper.selConsultDetail(map);
    }

    @Override
    public List<Map<String, Object>> selHealthConByPage(Map<String, Object> map) {
        return consultationMapper.selHealthConByPage(map);
    }

    @Override
    public HealthConsultation selHealthConById(long cid) {
        Map<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        return consultationMapper.selHealthConById(map);
    }

    @Override
    public List<QuestionAndReplyResource> selQuestionAndReplyByConId(long cid) {
        Map<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        return consultationMapper.selQuestionAndReply(map);
    }
}
