# spring-mybatis-starter

## SpringBoot with MyBatis 설정
### Dependencies
> ```groovy
> implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3'
> ```

### application.yml
> ```yaml
> mybatis:
>     configuration:
>         map-underscore-to-camel-case: true
>     mapper-locations: classpath:/mapper/**/*.xml
> ```
> 
> resources 파일 밑에 mapper-locations 에 지정한 디렉터리인 mapper 디렉터리를 생성 한 후 
> SQL 쿼리를 담은 .xml 파일을 둔다.
>
> 주의: test 의 resource 디렉터리 및 application.yml 을 따로 관리하는 경우 MyBatis 를 테스트하기 위해서
> test/resources/application.yml 에도 위의 내용을 추가해야 한다.

---

## Mapper
### Mapper 클래스
> Mapper 클래스는 인터페이스로 선언한다.  
> ```java
> @Repository
> @Mapper
> public interface UserDao {
>     List<User> findById(@Param("id") Long id);
> 
>     List<User> findByUuid(@Param("uuid") UUID uuid);
> }
> ```
> @Repository 애너테이션을 통해 저장소 Bean 객체로 등록된다.  
> @Mapper 애너테이션을 통해서 MyBatis Mapper Scan 의 대상이 된어 맵퍼 xml 과 맵퍼 클래스를 연결해준다.  
> 
> 맵퍼 xml 과 맵퍼 클래스는 이름을 동일하게 맞춰준다. 여기서는 클래스 명이 `UserDao` 이기 때문에 xml 이름도 `UserDao.xml` 로 한다.  
> 맵퍼 클래스의 메서드 명과 맵퍼 xml 의 id 도 이름을 동일하게 맞춰준다. 여기서는 `findById`, `findByUuid` 메서드가 있으므로 
> 맵퍼 xml 에 <select id="findById">...</select> 및 <select id="findByUuid">...</select> 가 있어야한다.

### xml:resultMap
> resultMap 은 맵팡하려는 Java 객체의 생성자 및 Setter 메서드를 이용한다.
> 
> 맵핑하려는 Java 객체에 생성자를 사용해서 칼럼 값들을 넘긴다면 <resultMap> 태그 안에 <result> 태그를 선언할 필요가 없다.   
> <resultMap> 태그 안에 <result> 태그를 선언하면 해당 칼럼은 Setter 를 통해서 맵핑된다.  
> 
> Mapping 하려는 Java 객체 안에 또 객체가 존재하는 경우 <association> 태그를 사용한다.
> ```java
> @AllArgsConstructor
> @Getter
> class PhoneNumber {
>     private String countryCode;
>     private String number;
> }
> 
> @AllArgsConstructor
> @Getter
> @NoArgsConstructor(access = AccessLevel.PROTECTED)
> @Setter(AccessLevel.PROTECTED)
> public class User {
>     private Long id;
>     private UUID uuid;
>     private PhoneNumber phoneNumber;
> }
> ```
> 맵핑하려는 객체는 User 이고 User 안에는 PhoneNumber 객체가 존재한다.
> 이와 같은 경우에 resultMap 은 다음과 같이 정의할 수 있다.
> ```xml
> <resultMap id="phoneNumberResultMap" type="starter.mybatis.domain.PhoneNumber">
> </resultMap>
> 
> <resultMap id="userResultMap" type="starter.mybatis.domain.User">
>     <result property="id" column="id"/>
>     <result property="uuid" column="uuid" javaType="java.util.UUID"
>                typeHandler="starter.mybatis.typehandler.UUIDTypeHandler"/>
>     <!-- 주의: association 태그는 result 태그보다 아래에 있어야한다. -->
>     <association property="phoneNumber" resultMap="phoneNumberResultMap"/>
> </resultMap>
> ```
> 여기서 phoneNumberResultMap 태그 내부에 아무런 <result> 태그가 없는 이유는 PhoneNumber 객체 맵핑 시
> 생성자를 사용한다는 의미이다.
> 또한 association 태그는 <result> 태그보다 아래에 있어야한다.
> 
> 다만 주의할 점은 생성자를 통해서 맵핑할 경우 SQL 쿼리 문과 생성자의 변수 순서가 동일해야하며 association 객체의 경우에는
> 생성자를 통해서 맵핑할 수 없기 때문에 생성자에서 제외해야한다. 
> 또한 association 객체와 관련된 칼럼의 경우 SQL 쿼리 문에서도 가장 아래에 선언해야하기 때문에 신경써야할 것이 많아진다.
> 그래서 association 태그를 사용한다면 즉 맵핑하려는 객체 안에 객체가 또 존재하는 경우에는 
> @NoArgsConstructor(access = AccessLevel.PROTECTED) 선언 및
> @Setter(AccessLevel.PROTECTED) 를 선언해서 Setter 로 맵핑하는 것을 권장한다.

### 동일 테이블 조인
> 동일 테이블을 여러번 조인할 시 각 테이블에 대해서 resultMap 따로 만들어야함. 
> 예를 들어 chatrooms 테이블에서 operator 와 participant 로 users 테이블 조인을 여러번 조인하는 경우에
> operator, participant 모두 users 테이블이라고 해서 resultMap 으로 userResultMap 을 하나로 사용하면 비정상 동작함.
> operatorResultMap 및 participantResultMap 을 따로 만들어서 적용해줘야함.
> ```xml
> <resultMap id="operatorResultMap" type="starter.mybatis.domain.ParticipantView">
>     <result property="userId" column="operator_id"/>
>     <result property="name" column="operator_name"/>
> </resultMap>
> <resultMap id="participantResultMap" type="starter.mybatis.domain.ParticipantView">
>     <result property="userId" column="participant_id"/>
>     <result property="name" column="participant_name"/>
> </resultMap>
> <resultMap id="chatroomResultMap" type="starter.mybatis.domain.ChatRoomView">
>     ...
>     <association property="operator" resultMap="operatorResultMap"/>
>     <collection property="participants" resultMap="participantResultMap"/>
> </resultMap>
> 
> <select id="findById" resultMap="chatroomResultMap">
>     select
>         c.id as chatroom_id, 
>         c.title as title.
>         o.id as operator_id,
>         o.name as operator_name,
>         p.id as participant_id,
>         p.name as participant_name
>     from chatrooms c
>     inner join users as o on c.operator_id = o.id
>     inner join chatroom_participants cp on c.id = cp.chatroom_id
>     inner join users as p on cp.id = p.id
> </select>
> ```

### TypeHandler
> MyBatis 에서 제공하지 않는 데이터타입을 맵퍼 클래스 메서드의 @Param 으로 받거나 resultMap 을 통해서 맵핑 객체에
> 데이터타입을 주입할 때에는 사용자가 커스텀으로 TypeHandler 를 만들어서 MyBatis 설정에 주입해야한다.  
> 
> 주의: Java 8 의 LocalDate, LocalDateTime, LocalTime 등은 MyBatis 에서 기본 지원하기 때문에 따로 만들지 않는다.
> 
> UUIDTypeHandler.java
> ```java
> public class UUIDTypeHandler extends BaseTypeHandler<UUID> {
> 
>     @Override
>     public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
>         ps.setString(i, parameter.toString());
>     }
> 
>     @Override
>     public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
>         return toUUID(rs.getBytes(columnName));
>     }
> 
>     @Override
>     public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
>         return toUUID(rs.getBytes(columnIndex));
>     }
> 
>     @Override
>     public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
>         return toUUID(cs.getBytes(columnIndex));
>     }
> 
>     private UUID toUUID(byte[] bytes) {
>         ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
>         long mostSignificantBits = byteBuffer.getLong();
>         long leastSignificantBits = byteBuffer.getLong();
>         return new UUID(mostSignificantBits, leastSignificantBits);
>     }
> }
> ```
> MyBatis 설정에 주입하게되면 resultMap 에 TypeHandler 를 일일히 명시하지 않아도 된다.
> MyBatisConfig.java
> ```java
> @Configuration
> public class MyBatisConfig {
> 
>     @Bean
>     public ConfigurationCustomizer mybatisConfigurationCustomizer() {
>         return configuration -> {
>             configuration.getTypeHandlerRegistry().register(UUID.class, JdbcType.BINARY, new UUIDTypeHandler());
>         };
>     }
> }
> ```
