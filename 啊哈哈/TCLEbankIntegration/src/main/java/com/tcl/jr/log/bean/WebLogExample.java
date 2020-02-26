package com.tcl.jr.log.bean;

import java.util.ArrayList;
import java.util.List;

public class WebLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WebLogExample() {
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

        public Criteria andReqIndexIsNull() {
            addCriterion("REQ_INDEX is null");
            return (Criteria) this;
        }

        public Criteria andReqIndexIsNotNull() {
            addCriterion("REQ_INDEX is not null");
            return (Criteria) this;
        }

        public Criteria andReqIndexEqualTo(String value) {
            addCriterion("REQ_INDEX =", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexNotEqualTo(String value) {
            addCriterion("REQ_INDEX <>", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexGreaterThan(String value) {
            addCriterion("REQ_INDEX >", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexGreaterThanOrEqualTo(String value) {
            addCriterion("REQ_INDEX >=", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexLessThan(String value) {
            addCriterion("REQ_INDEX <", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexLessThanOrEqualTo(String value) {
            addCriterion("REQ_INDEX <=", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexLike(String value) {
            addCriterion("REQ_INDEX like", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexNotLike(String value) {
            addCriterion("REQ_INDEX not like", value, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexIn(List<String> values) {
            addCriterion("REQ_INDEX in", values, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexNotIn(List<String> values) {
            addCriterion("REQ_INDEX not in", values, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexBetween(String value1, String value2) {
            addCriterion("REQ_INDEX between", value1, value2, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqIndexNotBetween(String value1, String value2) {
            addCriterion("REQ_INDEX not between", value1, value2, "reqIndex");
            return (Criteria) this;
        }

        public Criteria andReqTimeIsNull() {
            addCriterion("REQ_TIME is null");
            return (Criteria) this;
        }

        public Criteria andReqTimeIsNotNull() {
            addCriterion("REQ_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andReqTimeEqualTo(String value) {
            addCriterion("REQ_TIME =", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeNotEqualTo(String value) {
            addCriterion("REQ_TIME <>", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeGreaterThan(String value) {
            addCriterion("REQ_TIME >", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeGreaterThanOrEqualTo(String value) {
            addCriterion("REQ_TIME >=", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeLessThan(String value) {
            addCriterion("REQ_TIME <", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeLessThanOrEqualTo(String value) {
            addCriterion("REQ_TIME <=", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeLike(String value) {
            addCriterion("REQ_TIME like", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeNotLike(String value) {
            addCriterion("REQ_TIME not like", value, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeIn(List<String> values) {
            addCriterion("REQ_TIME in", values, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeNotIn(List<String> values) {
            addCriterion("REQ_TIME not in", values, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeBetween(String value1, String value2) {
            addCriterion("REQ_TIME between", value1, value2, "reqTime");
            return (Criteria) this;
        }

        public Criteria andReqTimeNotBetween(String value1, String value2) {
            addCriterion("REQ_TIME not between", value1, value2, "reqTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeIsNull() {
            addCriterion("REP_TIME is null");
            return (Criteria) this;
        }

        public Criteria andRepTimeIsNotNull() {
            addCriterion("REP_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andRepTimeEqualTo(String value) {
            addCriterion("REP_TIME =", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeNotEqualTo(String value) {
            addCriterion("REP_TIME <>", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeGreaterThan(String value) {
            addCriterion("REP_TIME >", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeGreaterThanOrEqualTo(String value) {
            addCriterion("REP_TIME >=", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeLessThan(String value) {
            addCriterion("REP_TIME <", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeLessThanOrEqualTo(String value) {
            addCriterion("REP_TIME <=", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeLike(String value) {
            addCriterion("REP_TIME like", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeNotLike(String value) {
            addCriterion("REP_TIME not like", value, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeIn(List<String> values) {
            addCriterion("REP_TIME in", values, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeNotIn(List<String> values) {
            addCriterion("REP_TIME not in", values, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeBetween(String value1, String value2) {
            addCriterion("REP_TIME between", value1, value2, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepTimeNotBetween(String value1, String value2) {
            addCriterion("REP_TIME not between", value1, value2, "repTime");
            return (Criteria) this;
        }

        public Criteria andRepDescIsNull() {
            addCriterion("REP_DESC is null");
            return (Criteria) this;
        }

        public Criteria andRepDescIsNotNull() {
            addCriterion("REP_DESC is not null");
            return (Criteria) this;
        }

        public Criteria andRepDescEqualTo(String value) {
            addCriterion("REP_DESC =", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescNotEqualTo(String value) {
            addCriterion("REP_DESC <>", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescGreaterThan(String value) {
            addCriterion("REP_DESC >", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescGreaterThanOrEqualTo(String value) {
            addCriterion("REP_DESC >=", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescLessThan(String value) {
            addCriterion("REP_DESC <", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescLessThanOrEqualTo(String value) {
            addCriterion("REP_DESC <=", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescLike(String value) {
            addCriterion("REP_DESC like", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescNotLike(String value) {
            addCriterion("REP_DESC not like", value, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescIn(List<String> values) {
            addCriterion("REP_DESC in", values, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescNotIn(List<String> values) {
            addCriterion("REP_DESC not in", values, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescBetween(String value1, String value2) {
            addCriterion("REP_DESC between", value1, value2, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepDescNotBetween(String value1, String value2) {
            addCriterion("REP_DESC not between", value1, value2, "repDesc");
            return (Criteria) this;
        }

        public Criteria andRepCodeIsNull() {
            addCriterion("REP_CODE is null");
            return (Criteria) this;
        }

        public Criteria andRepCodeIsNotNull() {
            addCriterion("REP_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andRepCodeEqualTo(String value) {
            addCriterion("REP_CODE =", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeNotEqualTo(String value) {
            addCriterion("REP_CODE <>", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeGreaterThan(String value) {
            addCriterion("REP_CODE >", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeGreaterThanOrEqualTo(String value) {
            addCriterion("REP_CODE >=", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeLessThan(String value) {
            addCriterion("REP_CODE <", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeLessThanOrEqualTo(String value) {
            addCriterion("REP_CODE <=", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeLike(String value) {
            addCriterion("REP_CODE like", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeNotLike(String value) {
            addCriterion("REP_CODE not like", value, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeIn(List<String> values) {
            addCriterion("REP_CODE in", values, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeNotIn(List<String> values) {
            addCriterion("REP_CODE not in", values, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeBetween(String value1, String value2) {
            addCriterion("REP_CODE between", value1, value2, "repCode");
            return (Criteria) this;
        }

        public Criteria andRepCodeNotBetween(String value1, String value2) {
            addCriterion("REP_CODE not between", value1, value2, "repCode");
            return (Criteria) this;
        }

        public Criteria andCmstrannoIsNull() {
            addCriterion("CMSTRANNO is null");
            return (Criteria) this;
        }

        public Criteria andCmstrannoIsNotNull() {
            addCriterion("CMSTRANNO is not null");
            return (Criteria) this;
        }

        public Criteria andCmstrannoEqualTo(String value) {
            addCriterion("CMSTRANNO =", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoNotEqualTo(String value) {
            addCriterion("CMSTRANNO <>", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoGreaterThan(String value) {
            addCriterion("CMSTRANNO >", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoGreaterThanOrEqualTo(String value) {
            addCriterion("CMSTRANNO >=", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoLessThan(String value) {
            addCriterion("CMSTRANNO <", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoLessThanOrEqualTo(String value) {
            addCriterion("CMSTRANNO <=", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoLike(String value) {
            addCriterion("CMSTRANNO like", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoNotLike(String value) {
            addCriterion("CMSTRANNO not like", value, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoIn(List<String> values) {
            addCriterion("CMSTRANNO in", values, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoNotIn(List<String> values) {
            addCriterion("CMSTRANNO not in", values, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoBetween(String value1, String value2) {
            addCriterion("CMSTRANNO between", value1, value2, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andCmstrannoNotBetween(String value1, String value2) {
            addCriterion("CMSTRANNO not between", value1, value2, "cmstranno");
            return (Criteria) this;
        }

        public Criteria andWebCodeIsNull() {
            addCriterion("WEB_CODE is null");
            return (Criteria) this;
        }

        public Criteria andWebCodeIsNotNull() {
            addCriterion("WEB_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andWebCodeEqualTo(String value) {
            addCriterion("WEB_CODE =", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeNotEqualTo(String value) {
            addCriterion("WEB_CODE <>", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeGreaterThan(String value) {
            addCriterion("WEB_CODE >", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeGreaterThanOrEqualTo(String value) {
            addCriterion("WEB_CODE >=", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeLessThan(String value) {
            addCriterion("WEB_CODE <", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeLessThanOrEqualTo(String value) {
            addCriterion("WEB_CODE <=", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeLike(String value) {
            addCriterion("WEB_CODE like", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeNotLike(String value) {
            addCriterion("WEB_CODE not like", value, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeIn(List<String> values) {
            addCriterion("WEB_CODE in", values, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeNotIn(List<String> values) {
            addCriterion("WEB_CODE not in", values, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeBetween(String value1, String value2) {
            addCriterion("WEB_CODE between", value1, value2, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebCodeNotBetween(String value1, String value2) {
            addCriterion("WEB_CODE not between", value1, value2, "webCode");
            return (Criteria) this;
        }

        public Criteria andWebDescIsNull() {
            addCriterion("WEB_DESC is null");
            return (Criteria) this;
        }

        public Criteria andWebDescIsNotNull() {
            addCriterion("WEB_DESC is not null");
            return (Criteria) this;
        }

        public Criteria andWebDescEqualTo(String value) {
            addCriterion("WEB_DESC =", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescNotEqualTo(String value) {
            addCriterion("WEB_DESC <>", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescGreaterThan(String value) {
            addCriterion("WEB_DESC >", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescGreaterThanOrEqualTo(String value) {
            addCriterion("WEB_DESC >=", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescLessThan(String value) {
            addCriterion("WEB_DESC <", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescLessThanOrEqualTo(String value) {
            addCriterion("WEB_DESC <=", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescLike(String value) {
            addCriterion("WEB_DESC like", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescNotLike(String value) {
            addCriterion("WEB_DESC not like", value, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescIn(List<String> values) {
            addCriterion("WEB_DESC in", values, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescNotIn(List<String> values) {
            addCriterion("WEB_DESC not in", values, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescBetween(String value1, String value2) {
            addCriterion("WEB_DESC between", value1, value2, "webDesc");
            return (Criteria) this;
        }

        public Criteria andWebDescNotBetween(String value1, String value2) {
            addCriterion("WEB_DESC not between", value1, value2, "webDesc");
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