package com.example.transport.model;

import java.util.ArrayList;
import java.util.List;

public class SysTranExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SysTranExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andTranNameIsNull() {
            addCriterion("tran_name is null");
            return (Criteria) this;
        }

        public Criteria andTranNameIsNotNull() {
            addCriterion("tran_name is not null");
            return (Criteria) this;
        }

        public Criteria andTranNameEqualTo(String value) {
            addCriterion("tran_name =", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameNotEqualTo(String value) {
            addCriterion("tran_name <>", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameGreaterThan(String value) {
            addCriterion("tran_name >", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameGreaterThanOrEqualTo(String value) {
            addCriterion("tran_name >=", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameLessThan(String value) {
            addCriterion("tran_name <", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameLessThanOrEqualTo(String value) {
            addCriterion("tran_name <=", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameLike(String value) {
            addCriterion("tran_name like", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameNotLike(String value) {
            addCriterion("tran_name not like", value, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameIn(List<String> values) {
            addCriterion("tran_name in", values, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameNotIn(List<String> values) {
            addCriterion("tran_name not in", values, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameBetween(String value1, String value2) {
            addCriterion("tran_name between", value1, value2, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranNameNotBetween(String value1, String value2) {
            addCriterion("tran_name not between", value1, value2, "tranName");
            return (Criteria) this;
        }

        public Criteria andTranTelIsNull() {
            addCriterion("tran_tel is null");
            return (Criteria) this;
        }

        public Criteria andTranTelIsNotNull() {
            addCriterion("tran_tel is not null");
            return (Criteria) this;
        }

        public Criteria andTranTelEqualTo(String value) {
            addCriterion("tran_tel =", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelNotEqualTo(String value) {
            addCriterion("tran_tel <>", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelGreaterThan(String value) {
            addCriterion("tran_tel >", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelGreaterThanOrEqualTo(String value) {
            addCriterion("tran_tel >=", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelLessThan(String value) {
            addCriterion("tran_tel <", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelLessThanOrEqualTo(String value) {
            addCriterion("tran_tel <=", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelLike(String value) {
            addCriterion("tran_tel like", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelNotLike(String value) {
            addCriterion("tran_tel not like", value, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelIn(List<String> values) {
            addCriterion("tran_tel in", values, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelNotIn(List<String> values) {
            addCriterion("tran_tel not in", values, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelBetween(String value1, String value2) {
            addCriterion("tran_tel between", value1, value2, "tranTel");
            return (Criteria) this;
        }

        public Criteria andTranTelNotBetween(String value1, String value2) {
            addCriterion("tran_tel not between", value1, value2, "tranTel");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlIsNull() {
            addCriterion("id_front_url is null");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlIsNotNull() {
            addCriterion("id_front_url is not null");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlEqualTo(String value) {
            addCriterion("id_front_url =", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlNotEqualTo(String value) {
            addCriterion("id_front_url <>", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlGreaterThan(String value) {
            addCriterion("id_front_url >", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlGreaterThanOrEqualTo(String value) {
            addCriterion("id_front_url >=", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlLessThan(String value) {
            addCriterion("id_front_url <", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlLessThanOrEqualTo(String value) {
            addCriterion("id_front_url <=", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlLike(String value) {
            addCriterion("id_front_url like", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlNotLike(String value) {
            addCriterion("id_front_url not like", value, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlIn(List<String> values) {
            addCriterion("id_front_url in", values, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlNotIn(List<String> values) {
            addCriterion("id_front_url not in", values, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlBetween(String value1, String value2) {
            addCriterion("id_front_url between", value1, value2, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdFrontUrlNotBetween(String value1, String value2) {
            addCriterion("id_front_url not between", value1, value2, "idFrontUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlIsNull() {
            addCriterion("id_back_url is null");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlIsNotNull() {
            addCriterion("id_back_url is not null");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlEqualTo(String value) {
            addCriterion("id_back_url =", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlNotEqualTo(String value) {
            addCriterion("id_back_url <>", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlGreaterThan(String value) {
            addCriterion("id_back_url >", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlGreaterThanOrEqualTo(String value) {
            addCriterion("id_back_url >=", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlLessThan(String value) {
            addCriterion("id_back_url <", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlLessThanOrEqualTo(String value) {
            addCriterion("id_back_url <=", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlLike(String value) {
            addCriterion("id_back_url like", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlNotLike(String value) {
            addCriterion("id_back_url not like", value, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlIn(List<String> values) {
            addCriterion("id_back_url in", values, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlNotIn(List<String> values) {
            addCriterion("id_back_url not in", values, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlBetween(String value1, String value2) {
            addCriterion("id_back_url between", value1, value2, "idBackUrl");
            return (Criteria) this;
        }

        public Criteria andIdBackUrlNotBetween(String value1, String value2) {
            addCriterion("id_back_url not between", value1, value2, "idBackUrl");
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