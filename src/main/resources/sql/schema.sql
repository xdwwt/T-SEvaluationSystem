-- 教学评价系统数据库表结构

-- 1. 用户登录表
CREATE TABLE IF NOT EXISTS tb_user_info (
    id              BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id         VARCHAR(20) NOT NULL UNIQUE COMMENT '登录账号',
    username        VARCHAR(50) COMMENT '用户姓名',
    password        VARCHAR(100) NOT NULL COMMENT '登录密码',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '身份 0:管理员 1:教师 2:学生',
    info_id         BIGINT COMMENT '关联教师/学生表ID，管理员为NULL',
    is_dele         TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_time DATETIME COMMENT '最后登录时间'
) COMMENT='用户登录表';

-- 2. 教师信息表
CREATE TABLE IF NOT EXISTS tb_teacher_info (
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    teacher_no  VARCHAR(20) NOT NULL UNIQUE COMMENT '教师工号',
    name        VARCHAR(50) NOT NULL COMMENT '姓名',
    gender      TINYINT COMMENT '性别 0:女 1:男',
    title       VARCHAR(20) COMMENT '职称（教授/副教授/讲师等）',
    department  VARCHAR(50) COMMENT '所属院系',
    phone       VARCHAR(20) COMMENT '电话',
    email       VARCHAR(50) COMMENT '邮箱',
    entry_date  DATE COMMENT '入职日期',
    is_dele     TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='教师信息表';

-- 3. 学生信息表
CREATE TABLE IF NOT EXISTS tb_student_info (
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    student_no  VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name        VARCHAR(50) NOT NULL COMMENT '姓名',
    gender      TINYINT COMMENT '性别 0:女 1:男',
    grade       VARCHAR(10) COMMENT '年级',
    major       VARCHAR(50) COMMENT '专业',
    phone       VARCHAR(20) COMMENT '电话',
    email       VARCHAR(50) COMMENT '邮箱',
    is_dele     TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='学生信息表';

-- 4. 课程表
CREATE TABLE IF NOT EXISTS tb_course (
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    course_code VARCHAR(20) NOT NULL UNIQUE COMMENT '课程编号',
    course_name VARCHAR(50) NOT NULL COMMENT '课程名称',
    credit      DECIMAL(3,1) COMMENT '学分',
    description TEXT COMMENT '课程描述',
    is_dele     TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='课程表';

-- 5. 班级表
CREATE TABLE IF NOT EXISTS tb_class_info (
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    class_name  VARCHAR(50) NOT NULL COMMENT '班级名称',
    grade       VARCHAR(10) COMMENT '年级',
    major       VARCHAR(50) COMMENT '专业',
    is_dele     TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='班级表';

-- 6. 班级-教师关联表
CREATE TABLE IF NOT EXISTS tb_class_teacher (
    id         BIGINT PRIMARY KEY COMMENT '主键ID',
    class_id   BIGINT NOT NULL COMMENT '班级ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    course_id  BIGINT NOT NULL COMMENT '课程ID',
    semester   VARCHAR(20) COMMENT '学期 如:2024-2025-1',
    is_dele    TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_class_teacher_course (class_id, teacher_id, course_id, semester)
) COMMENT='班级-教师关联表';

-- 7. 班级-学生关联表
CREATE TABLE IF NOT EXISTS tb_class_student (
    id         BIGINT PRIMARY KEY COMMENT '主键ID',
    class_id   BIGINT NOT NULL COMMENT '班级ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    is_dele    TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_class_student (class_id, student_id)
) COMMENT='班级-学生关联表';

-- 8. 成绩表
CREATE TABLE IF NOT EXISTS tb_score (
    id         BIGINT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    course_id  BIGINT NOT NULL COMMENT '课程ID',
    teacher_id BIGINT NOT NULL COMMENT '任课教师ID',
    class_id   BIGINT COMMENT '班级ID',
    semester     VARCHAR(20) COMMENT '学期',
    usual_score  DECIMAL(5,2) COMMENT '平时分',
    final_score  DECIMAL(5,2) COMMENT '期末考试分',
    score        DECIMAL(5,2) COMMENT '总评成绩',
    comment      TEXT COMMENT '评语',
    is_viewable  TINYINT DEFAULT 0 COMMENT '是否可查看 0:不可查看 1:可查看',
    is_dele      TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='成绩表';

-- 9. 教师评价表
CREATE TABLE IF NOT EXISTS tb_teacher_evaluation (
    id             BIGINT PRIMARY KEY COMMENT '主键ID',
    teacher_id     BIGINT NOT NULL COMMENT '被评价教师ID',
    student_id     BIGINT NOT NULL COMMENT '评价学生ID',
    course_id      BIGINT NOT NULL COMMENT '课程ID',
    semester       VARCHAR(20) COMMENT '学期',
    teaching_score INT COMMENT '教学态度评分 1-25',
    content_score  INT COMMENT '教学内容评分 1-25',
    method_score   INT COMMENT '教学方法评分 1-25',
    effect_score   INT COMMENT '教学效果评分 1-25',
    total_score    INT COMMENT '综合评分 1-100',
    is_dele        TINYINT DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='教师评价表';


ALTER TABLE tb_score
    ADD COLUMN usual_score DECIMAL(5,2) COMMENT '平时分',
    ADD COLUMN final_score DECIMAL(5,2) COMMENT '期末考试分',
    ADD COLUMN is_viewable TINYINT DEFAULT 0 COMMENT '是否可查看 0:不可查看 1:可查看';