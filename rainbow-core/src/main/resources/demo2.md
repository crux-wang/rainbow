# 用户模块

>

`/user` 

# 1. 更新用户资料

**POST**  `/user` 

**PATH VARIABLE**

|  name | type | required | default | comment text | other |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ || id | String | Y | 用户 ID |

**REQUEST BODY**

User

**RESPONSE**

User  //  用户资料

# 2. 删除用户

**DELETE**  `/user` 

**PATH VARIABLE**

|  name | type | required | default | comment text | other |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ || id | String | Y | 用户 ID |

**RESPONSE**

void  //  

# 3. 创建用户

**POST**  `/user` 

**REQUEST BODY**

User

**RESPONSE**

User  //  用户资料

# 4. 用户列表

**GET**  `/user` 

**REQUEST PARAM**

|  name | type | required | default | comment text | other |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ || pageable | Pageable | N | 分页信息 |

**RESPONSE**

Page  //  用户列表

# 实体列表

## 1. User

|  name | type | comment text | other |
| ------------ | ------------ | ------------ | ------------ || id | String | 用户 ID |
| nickname | String | 昵称Annotation(name=NotNull, type=javax.validation.constraints.NotNull, attribute=null, text=null),Annotation(name=Max, type=javax.validation.constraints.Max, attribute=null, text=null),Annotation(name=Min, type=javax.validation.constraints.Minattribute=null, text=null) |
| avatar | String | 头像Annotation(name=URL, type=org.hibernate.validator.constraints.URL, attribute=null, text=null) |
| email | String | 邮箱Annotation(name=Email, type=javax.validation.constraints.Email, attribute=null, text=null) |

## 2. Pageable


## 3. Page


