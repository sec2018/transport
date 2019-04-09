package com.example.transport.model;

import java.util.ArrayList;
import java.util.List;

public class SysShopExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SysShopExample() {
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

        public Criteria andShopIdIsNull() {
            addCriterion("shop_id is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("shop_id is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Integer value) {
            addCriterion("shop_id =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Integer value) {
            addCriterion("shop_id <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Integer value) {
            addCriterion("shop_id >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("shop_id >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Integer value) {
            addCriterion("shop_id <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Integer value) {
            addCriterion("shop_id <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Integer> values) {
            addCriterion("shop_id in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Integer> values) {
            addCriterion("shop_id not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Integer value1, Integer value2) {
            addCriterion("shop_id between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Integer value1, Integer value2) {
            addCriterion("shop_id not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNull() {
            addCriterion("shop_name is null");
            return (Criteria) this;
        }

        public Criteria andShopNameIsNotNull() {
            addCriterion("shop_name is not null");
            return (Criteria) this;
        }

        public Criteria andShopNameEqualTo(String value) {
            addCriterion("shop_name =", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotEqualTo(String value) {
            addCriterion("shop_name <>", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThan(String value) {
            addCriterion("shop_name >", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameGreaterThanOrEqualTo(String value) {
            addCriterion("shop_name >=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThan(String value) {
            addCriterion("shop_name <", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLessThanOrEqualTo(String value) {
            addCriterion("shop_name <=", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameLike(String value) {
            addCriterion("shop_name like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotLike(String value) {
            addCriterion("shop_name not like", value, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameIn(List<String> values) {
            addCriterion("shop_name in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotIn(List<String> values) {
            addCriterion("shop_name not in", values, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameBetween(String value1, String value2) {
            addCriterion("shop_name between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopNameNotBetween(String value1, String value2) {
            addCriterion("shop_name not between", value1, value2, "shopName");
            return (Criteria) this;
        }

        public Criteria andShopProcityIsNull() {
            addCriterion("shop_procity is null");
            return (Criteria) this;
        }

        public Criteria andShopProcityIsNotNull() {
            addCriterion("shop_procity is not null");
            return (Criteria) this;
        }

        public Criteria andShopProcityEqualTo(String value) {
            addCriterion("shop_procity =", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityNotEqualTo(String value) {
            addCriterion("shop_procity <>", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityGreaterThan(String value) {
            addCriterion("shop_procity >", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityGreaterThanOrEqualTo(String value) {
            addCriterion("shop_procity >=", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityLessThan(String value) {
            addCriterion("shop_procity <", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityLessThanOrEqualTo(String value) {
            addCriterion("shop_procity <=", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityLike(String value) {
            addCriterion("shop_procity like", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityNotLike(String value) {
            addCriterion("shop_procity not like", value, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityIn(List<String> values) {
            addCriterion("shop_procity in", values, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityNotIn(List<String> values) {
            addCriterion("shop_procity not in", values, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityBetween(String value1, String value2) {
            addCriterion("shop_procity between", value1, value2, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopProcityNotBetween(String value1, String value2) {
            addCriterion("shop_procity not between", value1, value2, "shopProcity");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaIsNull() {
            addCriterion("shop_detailarea is null");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaIsNotNull() {
            addCriterion("shop_detailarea is not null");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaEqualTo(String value) {
            addCriterion("shop_detailarea =", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaNotEqualTo(String value) {
            addCriterion("shop_detailarea <>", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaGreaterThan(String value) {
            addCriterion("shop_detailarea >", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaGreaterThanOrEqualTo(String value) {
            addCriterion("shop_detailarea >=", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaLessThan(String value) {
            addCriterion("shop_detailarea <", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaLessThanOrEqualTo(String value) {
            addCriterion("shop_detailarea <=", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaLike(String value) {
            addCriterion("shop_detailarea like", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaNotLike(String value) {
            addCriterion("shop_detailarea not like", value, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaIn(List<String> values) {
            addCriterion("shop_detailarea in", values, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaNotIn(List<String> values) {
            addCriterion("shop_detailarea not in", values, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaBetween(String value1, String value2) {
            addCriterion("shop_detailarea between", value1, value2, "shopDetailarea");
            return (Criteria) this;
        }

        public Criteria andShopDetailareaNotBetween(String value1, String value2) {
            addCriterion("shop_detailarea not between", value1, value2, "shopDetailarea");
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

        public Criteria andShopTelIsNull() {
            addCriterion("shop_tel is null");
            return (Criteria) this;
        }

        public Criteria andShopTelIsNotNull() {
            addCriterion("shop_tel is not null");
            return (Criteria) this;
        }

        public Criteria andShopTelEqualTo(String value) {
            addCriterion("shop_tel =", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelNotEqualTo(String value) {
            addCriterion("shop_tel <>", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelGreaterThan(String value) {
            addCriterion("shop_tel >", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelGreaterThanOrEqualTo(String value) {
            addCriterion("shop_tel >=", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelLessThan(String value) {
            addCriterion("shop_tel <", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelLessThanOrEqualTo(String value) {
            addCriterion("shop_tel <=", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelLike(String value) {
            addCriterion("shop_tel like", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelNotLike(String value) {
            addCriterion("shop_tel not like", value, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelIn(List<String> values) {
            addCriterion("shop_tel in", values, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelNotIn(List<String> values) {
            addCriterion("shop_tel not in", values, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelBetween(String value1, String value2) {
            addCriterion("shop_tel between", value1, value2, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopTelNotBetween(String value1, String value2) {
            addCriterion("shop_tel not between", value1, value2, "shopTel");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusIsNull() {
            addCriterion("shopcheckstatus is null");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusIsNotNull() {
            addCriterion("shopcheckstatus is not null");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusEqualTo(Integer value) {
            addCriterion("shopcheckstatus =", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusNotEqualTo(Integer value) {
            addCriterion("shopcheckstatus <>", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusGreaterThan(Integer value) {
            addCriterion("shopcheckstatus >", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("shopcheckstatus >=", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusLessThan(Integer value) {
            addCriterion("shopcheckstatus <", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusLessThanOrEqualTo(Integer value) {
            addCriterion("shopcheckstatus <=", value, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusIn(List<Integer> values) {
            addCriterion("shopcheckstatus in", values, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusNotIn(List<Integer> values) {
            addCriterion("shopcheckstatus not in", values, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusBetween(Integer value1, Integer value2) {
            addCriterion("shopcheckstatus between", value1, value2, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopcheckstatusNotBetween(Integer value1, Integer value2) {
            addCriterion("shopcheckstatus not between", value1, value2, "shopcheckstatus");
            return (Criteria) this;
        }

        public Criteria andShopUrlIsNull() {
            addCriterion("shop_url is null");
            return (Criteria) this;
        }

        public Criteria andShopUrlIsNotNull() {
            addCriterion("shop_url is not null");
            return (Criteria) this;
        }

        public Criteria andShopUrlEqualTo(String value) {
            addCriterion("shop_url =", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlNotEqualTo(String value) {
            addCriterion("shop_url <>", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlGreaterThan(String value) {
            addCriterion("shop_url >", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlGreaterThanOrEqualTo(String value) {
            addCriterion("shop_url >=", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlLessThan(String value) {
            addCriterion("shop_url <", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlLessThanOrEqualTo(String value) {
            addCriterion("shop_url <=", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlLike(String value) {
            addCriterion("shop_url like", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlNotLike(String value) {
            addCriterion("shop_url not like", value, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlIn(List<String> values) {
            addCriterion("shop_url in", values, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlNotIn(List<String> values) {
            addCriterion("shop_url not in", values, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlBetween(String value1, String value2) {
            addCriterion("shop_url between", value1, value2, "shopUrl");
            return (Criteria) this;
        }

        public Criteria andShopUrlNotBetween(String value1, String value2) {
            addCriterion("shop_url not between", value1, value2, "shopUrl");
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