package com.yy.bscRobot.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserInfoExample() {
        oredCriteria = new ArrayList<>();
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
            criteria = new ArrayList<>();
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTelegramIdIsNull() {
            addCriterion("telegram_id is null");
            return (Criteria) this;
        }

        public Criteria andTelegramIdIsNotNull() {
            addCriterion("telegram_id is not null");
            return (Criteria) this;
        }

        public Criteria andTelegramIdEqualTo(String value) {
            addCriterion("telegram_id =", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdNotEqualTo(String value) {
            addCriterion("telegram_id <>", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdGreaterThan(String value) {
            addCriterion("telegram_id >", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdGreaterThanOrEqualTo(String value) {
            addCriterion("telegram_id >=", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdLessThan(String value) {
            addCriterion("telegram_id <", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdLessThanOrEqualTo(String value) {
            addCriterion("telegram_id <=", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdLike(String value) {
            addCriterion("telegram_id like", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdNotLike(String value) {
            addCriterion("telegram_id not like", value, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdIn(List<String> values) {
            addCriterion("telegram_id in", values, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdNotIn(List<String> values) {
            addCriterion("telegram_id not in", values, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdBetween(String value1, String value2) {
            addCriterion("telegram_id between", value1, value2, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramIdNotBetween(String value1, String value2) {
            addCriterion("telegram_id not between", value1, value2, "telegramId");
            return (Criteria) this;
        }

        public Criteria andTelegramNameIsNull() {
            addCriterion("telegram_name is null");
            return (Criteria) this;
        }

        public Criteria andTelegramNameIsNotNull() {
            addCriterion("telegram_name is not null");
            return (Criteria) this;
        }

        public Criteria andTelegramNameEqualTo(String value) {
            addCriterion("telegram_name =", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameNotEqualTo(String value) {
            addCriterion("telegram_name <>", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameGreaterThan(String value) {
            addCriterion("telegram_name >", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameGreaterThanOrEqualTo(String value) {
            addCriterion("telegram_name >=", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameLessThan(String value) {
            addCriterion("telegram_name <", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameLessThanOrEqualTo(String value) {
            addCriterion("telegram_name <=", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameLike(String value) {
            addCriterion("telegram_name like", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameNotLike(String value) {
            addCriterion("telegram_name not like", value, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameIn(List<String> values) {
            addCriterion("telegram_name in", values, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameNotIn(List<String> values) {
            addCriterion("telegram_name not in", values, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameBetween(String value1, String value2) {
            addCriterion("telegram_name between", value1, value2, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramNameNotBetween(String value1, String value2) {
            addCriterion("telegram_name not between", value1, value2, "telegramName");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenIsNull() {
            addCriterion("telegram_token is null");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenIsNotNull() {
            addCriterion("telegram_token is not null");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenEqualTo(String value) {
            addCriterion("telegram_token =", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenNotEqualTo(String value) {
            addCriterion("telegram_token <>", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenGreaterThan(String value) {
            addCriterion("telegram_token >", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenGreaterThanOrEqualTo(String value) {
            addCriterion("telegram_token >=", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenLessThan(String value) {
            addCriterion("telegram_token <", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenLessThanOrEqualTo(String value) {
            addCriterion("telegram_token <=", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenLike(String value) {
            addCriterion("telegram_token like", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenNotLike(String value) {
            addCriterion("telegram_token not like", value, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenIn(List<String> values) {
            addCriterion("telegram_token in", values, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenNotIn(List<String> values) {
            addCriterion("telegram_token not in", values, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenBetween(String value1, String value2) {
            addCriterion("telegram_token between", value1, value2, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andTelegramTokenNotBetween(String value1, String value2) {
            addCriterion("telegram_token not between", value1, value2, "telegramToken");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIsNull() {
            addCriterion("register_time is null");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIsNotNull() {
            addCriterion("register_time is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeEqualTo(Date value) {
            addCriterion("register_time =", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotEqualTo(Date value) {
            addCriterion("register_time <>", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeGreaterThan(Date value) {
            addCriterion("register_time >", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("register_time >=", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeLessThan(Date value) {
            addCriterion("register_time <", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeLessThanOrEqualTo(Date value) {
            addCriterion("register_time <=", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIn(List<Date> values) {
            addCriterion("register_time in", values, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotIn(List<Date> values) {
            addCriterion("register_time not in", values, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeBetween(Date value1, Date value2) {
            addCriterion("register_time between", value1, value2, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotBetween(Date value1, Date value2) {
            addCriterion("register_time not between", value1, value2, "registerTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("last_login_time is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("last_login_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("last_login_time =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("last_login_time <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("last_login_time >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_login_time >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("last_login_time <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_login_time <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("last_login_time in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("last_login_time not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("last_login_time between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_login_time not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andBanStatusIsNull() {
            addCriterion("ban_status is null");
            return (Criteria) this;
        }

        public Criteria andBanStatusIsNotNull() {
            addCriterion("ban_status is not null");
            return (Criteria) this;
        }

        public Criteria andBanStatusEqualTo(Integer value) {
            addCriterion("ban_status =", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusNotEqualTo(Integer value) {
            addCriterion("ban_status <>", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusGreaterThan(Integer value) {
            addCriterion("ban_status >", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("ban_status >=", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusLessThan(Integer value) {
            addCriterion("ban_status <", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusLessThanOrEqualTo(Integer value) {
            addCriterion("ban_status <=", value, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusIn(List<Integer> values) {
            addCriterion("ban_status in", values, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusNotIn(List<Integer> values) {
            addCriterion("ban_status not in", values, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusBetween(Integer value1, Integer value2) {
            addCriterion("ban_status between", value1, value2, "banStatus");
            return (Criteria) this;
        }

        public Criteria andBanStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("ban_status not between", value1, value2, "banStatus");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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