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