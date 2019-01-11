package com.example.transport.model;

import java.util.ArrayList;
import java.util.List;

public class CompanyLinesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CompanyLinesExample() {
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

        public Criteria andArriveAddrIsNull() {
            addCriterion("arrive_addr is null");
            return (Criteria) this;
        }

        public Criteria andArriveAddrIsNotNull() {
            addCriterion("arrive_addr is not null");
            return (Criteria) this;
        }

        public Criteria andArriveAddrEqualTo(String value) {
            addCriterion("arrive_addr =", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrNotEqualTo(String value) {
            addCriterion("arrive_addr <>", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrGreaterThan(String value) {
            addCriterion("arrive_addr >", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrGreaterThanOrEqualTo(String value) {
            addCriterion("arrive_addr >=", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrLessThan(String value) {
            addCriterion("arrive_addr <", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrLessThanOrEqualTo(String value) {
            addCriterion("arrive_addr <=", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrLike(String value) {
            addCriterion("arrive_addr like", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrNotLike(String value) {
            addCriterion("arrive_addr not like", value, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrIn(List<String> values) {
            addCriterion("arrive_addr in", values, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrNotIn(List<String> values) {
            addCriterion("arrive_addr not in", values, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrBetween(String value1, String value2) {
            addCriterion("arrive_addr between", value1, value2, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveAddrNotBetween(String value1, String value2) {
            addCriterion("arrive_addr not between", value1, value2, "arriveAddr");
            return (Criteria) this;
        }

        public Criteria andArriveTelIsNull() {
            addCriterion("arrive_tel is null");
            return (Criteria) this;
        }

        public Criteria andArriveTelIsNotNull() {
            addCriterion("arrive_tel is not null");
            return (Criteria) this;
        }

        public Criteria andArriveTelEqualTo(String value) {
            addCriterion("arrive_tel =", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelNotEqualTo(String value) {
            addCriterion("arrive_tel <>", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelGreaterThan(String value) {
            addCriterion("arrive_tel >", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelGreaterThanOrEqualTo(String value) {
            addCriterion("arrive_tel >=", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelLessThan(String value) {
            addCriterion("arrive_tel <", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelLessThanOrEqualTo(String value) {
            addCriterion("arrive_tel <=", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelLike(String value) {
            addCriterion("arrive_tel like", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelNotLike(String value) {
            addCriterion("arrive_tel not like", value, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelIn(List<String> values) {
            addCriterion("arrive_tel in", values, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelNotIn(List<String> values) {
            addCriterion("arrive_tel not in", values, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelBetween(String value1, String value2) {
            addCriterion("arrive_tel between", value1, value2, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andArriveTelNotBetween(String value1, String value2) {
            addCriterion("arrive_tel not between", value1, value2, "arriveTel");
            return (Criteria) this;
        }

        public Criteria andBeginAddrIsNull() {
            addCriterion("begin_addr is null");
            return (Criteria) this;
        }

        public Criteria andBeginAddrIsNotNull() {
            addCriterion("begin_addr is not null");
            return (Criteria) this;
        }

        public Criteria andBeginAddrEqualTo(String value) {
            addCriterion("begin_addr =", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrNotEqualTo(String value) {
            addCriterion("begin_addr <>", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrGreaterThan(String value) {
            addCriterion("begin_addr >", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrGreaterThanOrEqualTo(String value) {
            addCriterion("begin_addr >=", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrLessThan(String value) {
            addCriterion("begin_addr <", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrLessThanOrEqualTo(String value) {
            addCriterion("begin_addr <=", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrLike(String value) {
            addCriterion("begin_addr like", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrNotLike(String value) {
            addCriterion("begin_addr not like", value, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrIn(List<String> values) {
            addCriterion("begin_addr in", values, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrNotIn(List<String> values) {
            addCriterion("begin_addr not in", values, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrBetween(String value1, String value2) {
            addCriterion("begin_addr between", value1, value2, "beginAddr");
            return (Criteria) this;
        }

        public Criteria andBeginAddrNotBetween(String value1, String value2) {
            addCriterion("begin_addr not between", value1, value2, "beginAddr");
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