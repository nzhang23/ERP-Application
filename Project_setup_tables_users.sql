create table Model(model_ID char(10) not null, model_name varchar(20), sales_price numeric(20, 2), primary key(model_ID));
create table Customer(customer_ID char(10)  not null, first_name varchar(20), last_name varchar(20), primary key(customer_ID));
create table EmployeeLogin(employee_ID char(10) not null, user_ID char(10) not null, privilege varchar(20), first_name varchar(20), last_name varchar(20), ssn char(10), salary numeric(20, 2) check(salary>0), salary_type varchar(20), job_type varchar(20) check(job_type in ('HR','Sale','Engineer')), primary key(employee_ID));
create table Inventory(ID char(10) not null, cost numeric(20, 2) check(cost>0), lead_time int check (lead_time>0), model_ID char(10), number int check(number > -1),primary key(ID), foreign key(model_ID) references Model);
create table Orders(order_ID char(10) not null, sale_value numeric (20, 2) check(sale_value>0), employee_ID char(10), customer_ID char(10), year int check(year >0), primary key(order_ID), foreign key (employee_ID) references EmployeeLogin, foreign key (customer_ID) references Customer);
create table Choose(model_ID char(10) not null, order_ID char(10) not null, purchasing_amount int check(purchasing_amount>0), primary key(model_ID, order_ID), foreign key (model_ID) references Model, foreign key (order_ID) references Orders);
create table Sessions(employee_ID char(10) not null, login_time timestamp not null, logout_time timestamp not null, primary key(employee_ID, login_time, logout_time), foreign key (employee_ID) references EmployeeLogin);

create view TotalSaleV as (select sum(sale_value) from Orders group by year);
create view orderInventory as (select choose.model_ID, order_id, purchasing_amount, number from (choose natural join inventory));
create view modelReport as (select model_ID, year,sum(purchasing_amount) from Choose natural join Orders group by model_ID, year);

create view InventoryExpense as (select year, sum(cost * purchasing_amount) as Total_Cost from (inventory natural join Choose natural join Orders) group by year);

create view EmployeeExpense as (select sum(salary) as Total_Salary from employeelogin);

create view EarnedSale as (select year, sum(sales_price * purchasing_amount) as Total_Earn from (orders natural join model natural join choose) group by year);

create view totalExpense as (select year, Total_Salary + Total_Cost as Total_Expense from (InventoryExpense cross join EmployeeExpense));

create view TotalRevenue as (select year, Total_Earn - Total_Expense as Revenue from (EarnedSale natural join totalExpense));

create view BuisnessReport as (select year, Total_Cost, Total_Salary, Total_Expense from ((totalExpense natural join InventoryExpense)cross join EmployeeExpense));

ALTER ROLE "admin" SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN;

create role admin INHERIT LOGIN;
create role Sale INHERIT LOGIN;
create role HR INHERIT LOGIN;
create role Engineer INHERIT LOGIN;


create index ModelID_index on Model(model_ID);
create index CustomerID_index on Customer(customer_ID);
create index EmployeeLoginID_index on EmployeeLogin(employee_ID);
create index InventoryID_index on Inventory(ID);
create index OrdersID_index on Orders(order_ID);
create index ChooseID_index on Choose(model_ID, order_ID);
create index SessionsID_index on Sessions(employee_ID, login_time, logout_time);

create view jobtype as (select user_ID, privilege from EmployeeLogin);
grant create on database postgres to admin;
grant all privileges on Model to admin with grant option;
grant all privileges on Customer to admin with grant option;
grant all privileges on employeelogin to admin with grant option;
grant all privileges on Inventory to admin with grant option;
grant all privileges on Orders to admin with grant option;
grant all privileges on Choose to admin with grant option;
grant all privileges on Sessions to admin with grant option;
grant all privileges on jobtype to admin with grant option;
grant all privileges on TotalSaleV to admin with grant option;
grant all privileges on orderInventory to admin with grant option;
grant all privileges on modelReport to admin with grant option;
grant all privileges on InventoryExpense to admin with grant option;
grant all privileges on EmployeeExpense to admin with grant option;
grant all privileges on EarnedSale to admin with grant option;
grant all privileges on totalExpense to admin with grant option;
grant all privileges on TotalRevenue to admin with grant option;
grant all privileges on BuisnessReport to admin with grant option;

grant select on jobtype to sale;
GRANT UPDATE ON customer TO sale;
GRANT SELECT ON customer TO sale;
grant insert on customer to sale;
GRANT INSERT ON Orders TO sale;
GRANT SELECT ON Orders TO sale;
GRANT select ON Inventory TO sale;
GRANT update ON Inventory TO sale;
GRANT select ON Choose TO sale;
GRANT select ON Model TO sale;
GRANT insert ON Choose TO sale;
GRANT select ON TotalRevenue TO sale;
GRANT select ON modelReport TO sale;

grant select on jobtype to Engineer;
grant update on Model to Engineer;
grant select on Model to Engineer;
grant insert on Model to Engineer;
grant update on Inventory to Engineer;
grant select on Inventory to Engineer;
grant insert on Inventory to Engineer;
create view enginView as(select first_name, last_name, job_type from Employeelogin);
grant select on enginView to Engineer;
grant select on orderInventory to Engineer;

grant select on jobtype to HR;
grant update on Employeelogin to HR;
grant select on Employeelogin to HR;
grant insert on Employeelogin to HR;
create view emploSales as (select Employeelogin.employee_id, first_name, last_name, ssn, salary, salary_type, job_type, sum(sale_value) from Employeelogin natural left outer join Orders group by Employeelogin.employee_id);
grant select on emploSales to HR;

insert into employeelogin values('e000000001', 'u000000001','Admin', 'Ning', 'Zhang','123457890','100000','salaried', 'Engineer'); 
create user u000000001 with password '1' inherit login;
ALTER ROLE "u000000001" SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN;
grant admin to u000000001;


insert into employeelogin values('e000000002', 'u000000002','HR', 'Bob', 'Green','123457891','100000','salaried', 'HR'); 
create user u000000002  with password '1' inherit login;
grant HR to u000000002;

insert into employeelogin values('e000000003', 'u000000003','Sale', 'Ann', 'Allcock','123457892','100000','salaried', 'Sale'); 
create user u000000003  with password '1' inherit login;
grant Sale to u000000003;

insert into employeelogin values('e000000004', 'u000000004','Engineer', 'Bill', 'Gates','123457893','100000','salaried', 'Engineer'); 
create user u000000004  with password '1' inherit login;
grant Engineer to u000000004;

insert into employeelogin values('e000000005', 'u000000005','Sale', 'Newton', 'Willson','123457894','100000','salaried', 'Sale'); 
create user u000000005  with password '1'inherit login;
grant Sale to u000000005;



insert  into model values('m000000001','Model_S', 100000);
insert  into model values('m000000002','Model_E', 50000);
insert  into model values('m000000003','Model_X', 120000);
insert  into model values('m000000004','Model_Y', 150000);

insert  into customer values('c000000001','Dan', 'Ford');
insert  into customer values('c000000002','Hanna', 'White');

insert into inventory values('i000000001',20000,30,'m000000001',30);
insert into inventory values('i000000002',10000,30,'m000000002',30);
insert into inventory values('i000000003',60000,30,'m000000003',30);
insert into inventory values('i000000004',100000,30,'m000000004',30);

insert into orders values('o000000001',500000,'e000000003','c000000001',2018);
insert into orders values('o000000002',3200000,'e000000005','c000000002',2019);

insert into choose values('m000000001','o000000001',5);
insert into choose values('m000000002','o000000002',10);
insert into choose values('m000000003','o000000002',10);
insert into choose values('m000000004','o000000002',10);

insert into sessions values('e000000001', '2020-04-29 19:00:00', '2020-04-29 23:00:00');
insert into sessions values('e000000002', '2020-05-29 19:00:00', '2020-05-29 23:00:00');





