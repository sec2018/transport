package com.example.transport.model;

import java.util.ArrayList;
import java.util.List;

public class SysCompanyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SysCompanyExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCompanyIdIsNull() {
            addCriterion("company_id is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNotNull() {
            addCriterion("company_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdEqualTo(Integer value) {
            addCriterion("company_id =", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotEqualTo(Integer value) {
            addCriterion("company_id <>", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThan(Integer value) {
            addCriterion("company_id >", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("company_id >=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThan(Integer value) {
            addCriterion("company_id <", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThanOrEqualTo(Integer value) {
            addCriterion("company_id <=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIn(List<Integer> values) {
            addCriterion("company_id in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotIn(List<Integer> values) {
            addCriterion("company_id not in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdBetween(Integer value1, Integer value2) {
            addCriterion("company_id between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("company_id not between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyNameIsNull() {
            addCriterion("company_name is null");
            return (Criteria) this;
        }

        public Criteria andCompanyNameIsNotNull() {
            addCriterion("company_name is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyNameEqualTo(String value) {
            addCriterion("company_name =", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameNotEqualTo(String value) {
            addCriterion("company_name <>", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameGreaterThan(String value) {
            addCriterion("company_name >", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameGreaterThanOrEqualTo(String value) {
            addCriterion("company_name >=", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameLessThan(String value) {
            addCriterion("company_name <", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameLessThanOrEqualTo(String value) {
            addCriterion("company_name <=", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameLike(String value) {
            addCriterion("company_name like", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameNotLike(String value) {
            addCriterion("company_name not like", value, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameIn(List<String> values) {
            addCriterion("company_name in", values, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameNotIn(List<String> values) {
            addCriterion("company_name not in", values, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameBetween(String value1, String value2) {
            addCriterion("company_name between", value1, value2, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyNameNotBetween(String value1, String value2) {
            addCriterion("company_name not between", value1, value2, "companyName");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityIsNull() {
            addCriterion("company_procity is null");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityIsNotNull() {
            addCriterion("company_procity is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityEqualTo(String value) {
            addCriterion("company_procity =", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityNotEqualTo(String value) {
            addCriterion("company_procity <>", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityGreaterThan(String value) {
            addCriterion("company_procity >", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityGreaterThanOrEqualTo(String value) {
            addCriterion("company_procity >=", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityLessThan(String value) {
            addCriterion("company_procity <", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityLessThanOrEqualTo(String value) {
            addCriterion("company_procity <=", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityLike(String value) {
            addCriterion("company_procity like", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityNotLike(String value) {
            addCriterion("company_procity not like", value, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityIn(List<String> values) {
            addCriterion("company_procity in", values, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityNotIn(List<String> values) {
            addCriterion("company_procity not in", values, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityBetween(String value1, String value2) {
            addCriterion("company_procity between", value1, value2, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyProcityNotBetween(String value1, String value2) {
            addCriterion("company_procity not between", value1, value2, "companyProcity");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaIsNull() {
            addCriterion("company_detailarea is null");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaIsNotNull() {
            addCriterion("company_detailarea is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaEqualTo(String value) {
            addCriterion("company_detailarea =", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaNotEqualTo(String value) {
            addCriterion("company_detailarea <>", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaGreaterThan(String value) {
            addCriterion("company_detailarea >", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaGreaterThanOrEqualTo(String value) {
            addCriterion("company_detailarea >=", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaLessThan(String value) {
            addCriterion("company_detailarea <", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaLessThanOrEqualTo(String value) {
            addCriterion("company_detailarea <=", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaLike(String value) {
            addCriterion("company_detailarea like", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaNotLike(String value) {
            addCriterion("company_detailarea not like", value, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaIn(List<String> values) {
            addCriterion("company_detailarea in", values, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaNotIn(List<String> values) {
            addCriterion("company_detailarea not in", values, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaBetween(String value1, String value2) {
            addCriterion("company_detailarea between", value1, value2, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andCompanyDetailareaNotBetween(String value1, String value2) {
            addCriterion("company_detailarea not between", value1, value2, "companyDetailarea");
            return (Criteria) this;
        }

        public Criteria andWxuserIdIsNull() {
            addCriterion("wxuser_id is null");
            return (Criteria) this;
        }

        public Criteria andWxuserIdIsNotNull() {
            addCriterion("wxuser_id is not null");
            return (Criteria) this;
        }

        public Criteria andWxuserIdEqualTo(Long value) {
            addCriterion("wxuser_id =", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdNotEqualTo(Long value) {
            addCriterion("wxuser_id <>", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdGreaterThan(Long value) {
            addCriterion("wxuser_id >", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("wxuser_id >=", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdLessThan(Long value) {
            addCriterion("wxuser_id <", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdLessThanOrEqualTo(Long value) {
            addCriterion("wxuser_id <=", value, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdIn(List<Long> values) {
            addCriterion("wxuser_id in", values, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdNotIn(List<Long> values) {
            addCriterion("wxuser_id not in", values, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdBetween(Long value1, Long value2) {
            addCriterion("wxuser_id between", value1, value2, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andWxuserIdNotBetween(Long value1, Long value2) {
            addCriterion("wxuser_id not between", value1, value2, "wxuserId");
            return (Criteria) this;
        }

        public Criteria andCompanyTelIsNull() {
            addCriterion("company_tel is null");
            return (Criteria) this;
        }

        public Criteria andCompanyTelIsNotNull() {
            addCriterion("company_tel is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyTelEqualTo(String value) {
            addCriterion("company_tel =", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelNotEqualTo(String value) {
            addCriterion("company_tel <>", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelGreaterThan(String value) {
            addCriterion("company_tel >", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelGreaterThanOrEqualTo(String value) {
            addCriterion("company_tel >=", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelLessThan(String value) {
            addCriterion("company_tel <", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelLessThanOrEqualTo(String value) {
            addCriterion("company_tel <=", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelLike(String value) {
            addCriterion("company_tel like", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelNotLike(String value) {
            addCriterion("company_tel not like", value, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelIn(List<String> values) {
            addCriterion("company_tel in", values, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelNotIn(List<String> values) {
            addCriterion("company_tel not in", values, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelBetween(String value1, String value2) {
            addCriterion("company_tel between", value1, value2, "companyTel");
            return (Criteria) this;
        }

        public Criteria andCompanyTelNotBetween(String value1, String value2) {
            addCriterion("company_tel not between", value1, value2, "companyTel");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlIsNull() {
            addCriterion("licence_url is null");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlIsNotNull() {
            addCriterion("licence_url is not null");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlEqualTo(String value) {
            addCriterion("licence_url =", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlNotEqualTo(String value) {
            addCriterion("licence_url <>", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlGreaterThan(String value) {
            addCriterion("licence_url >", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlGreaterThanOrEqualTo(String value) {
            addCriterion("licence_url >=", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlLessThan(String value) {
            addCriterion("licence_url <", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlLessThanOrEqualTo(String value) {
            addCriterion("licence_url <=", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlLike(String value) {
            addCriterion("licence_url like", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlNotLike(String value) {
            addCriterion("licence_url not like", value, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlIn(List<String> values) {
            addCriterion("licence_url in", values, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlNotIn(List<String> values) {
            addCriterion("licence_url not in", values, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlBetween(String value1, String value2) {
            addCriterion("licence_url between", value1, value2, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andLicenceUrlNotBetween(String value1, String value2) {
            addCriterion("licence_url not between", value1, value2, "licenceUrl");
            return (Criteria) this;
        }

        public Criteria andComplainTelIsNull() {
            addCriterion("complain_tel is null");
            return (Criteria) this;
        }

        public Criteria andComplainTelIsNotNull() {
            addCriterion("complain_tel is not null");
            return (Criteria) this;
        }

        public Criteria andComplainTelEqualTo(String value) {
            addCriterion("complain_tel =", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelNotEqualTo(String value) {
            addCriterion("complain_tel <>", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelGreaterThan(String value) {
            addCriterion("complain_tel >", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelGreaterThanOrEqualTo(String value) {
            addCriterion("complain_tel >=", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelLessThan(String value) {
            addCriterion("complain_tel <", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelLessThanOrEqualTo(String value) {
            addCriterion("complain_tel <=", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelLike(String value) {
            addCriterion("complain_tel like", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelNotLike(String value) {
            addCriterion("complain_tel not like", value, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelIn(List<String> values) {
            addCriterion("complain_tel in", values, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelNotIn(List<String> values) {
            addCriterion("complain_tel not in", values, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelBetween(String value1, String value2) {
            addCriterion("complain_tel between", value1, value2, "complainTel");
            return (Criteria) this;
        }

        public Criteria andComplainTelNotBetween(String value1, String value2) {
            addCriterion("complain_tel not between", value1, value2, "complainTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelIsNull() {
            addCriterion("service_tel is null");
            return (Criteria) this;
        }

        public Criteria andServiceTelIsNotNull() {
            addCriterion("service_tel is not null");
            return (Criteria) this;
        }

        public Criteria andServiceTelEqualTo(String value) {
            addCriterion("service_tel =", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelNotEqualTo(String value) {
            addCriterion("service_tel <>", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelGreaterThan(String value) {
            addCriterion("service_tel >", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelGreaterThanOrEqualTo(String value) {
            addCriterion("service_tel >=", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelLessThan(String value) {
            addCriterion("service_tel <", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelLessThanOrEqualTo(String value) {
            addCriterion("service_tel <=", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelLike(String value) {
            addCriterion("service_tel like", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelNotLike(String value) {
            addCriterion("service_tel not like", value, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelIn(List<String> values) {
            addCriterion("service_tel in", values, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelNotIn(List<String> values) {
            addCriterion("service_tel not in", values, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelBetween(String value1, String value2) {
            addCriterion("service_tel between", value1, value2, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andServiceTelNotBetween(String value1, String value2) {
            addCriterion("service_tel not between", value1, value2, "serviceTel");
            return (Criteria) this;
        }

        public Criteria andEvaluationIsNull() {
            addCriterion("evaluation is null");
            return (Criteria) this;
        }

        public Criteria andEvaluationIsNotNull() {
            addCriterion("evaluation is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluationEqualTo(Integer value) {
            addCriterion("evaluation =", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationNotEqualTo(Integer value) {
            addCriterion("evaluation <>", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationGreaterThan(Integer value) {
            addCriterion("evaluation >", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationGreaterThanOrEqualTo(Integer value) {
            addCriterion("evaluation >=", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationLessThan(Integer value) {
            addCriterion("evaluation <", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationLessThanOrEqualTo(Integer value) {
            addCriterion("evaluation <=", value, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationIn(List<Integer> values) {
            addCriterion("evaluation in", values, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationNotIn(List<Integer> values) {
            addCriterion("evaluation not in", values, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationBetween(Integer value1, Integer value2) {
            addCriterion("evaluation between", value1, value2, "evaluation");
            return (Criteria) this;
        }

        public Criteria andEvaluationNotBetween(Integer value1, Integer value2) {
            addCriterion("evaluation not between", value1, value2, "evaluation");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidIsNull() {
            addCriterion("default_lineid is null");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidIsNotNull() {
            addCriterion("default_lineid is not null");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidEqualTo(Integer value) {
            addCriterion("default_lineid =", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidNotEqualTo(Integer value) {
            addCriterion("default_lineid <>", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidGreaterThan(Integer value) {
            addCriterion("default_lineid >", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidGreaterThanOrEqualTo(Integer value) {
            addCriterion("default_lineid >=", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidLessThan(Integer value) {
            addCriterion("default_lineid <", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidLessThanOrEqualTo(Integer value) {
            addCriterion("default_lineid <=", value, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidIn(List<Integer> values) {
            addCriterion("default_lineid in", values, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidNotIn(List<Integer> values) {
            addCriterion("default_lineid not in", values, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidBetween(Integer value1, Integer value2) {
            addCriterion("default_lineid between", value1, value2, "defaultLineid");
            return (Criteria) this;
        }

        public Criteria andDefaultLineidNotBetween(Integer value1, Integer value2) {
            addCriterion("default_lineid not between", value1, value2, "defaultLineid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}