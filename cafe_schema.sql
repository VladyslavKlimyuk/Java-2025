--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: add_item_to_existing_order(integer, character varying, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.add_item_to_existing_order(order_id_param integer, item_type character varying, item_id_param integer, quantity_param integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    item_price NUMERIC(6, 2);
BEGIN
    IF item_type = 'DRINK' THEN
        SELECT Price INTO item_price FROM Drink WHERE DrinkID = item_id_param;
        INSERT INTO OrderDrink (OrderID, DrinkID, Quantity, ItemPrice)
        VALUES (order_id_param, item_id_param, quantity_param, item_price)
        ON CONFLICT (OrderID, DrinkID) DO UPDATE
        SET Quantity = OrderDrink.Quantity + quantity_param;

    ELSIF item_type = 'DESSERT' THEN
        SELECT Price INTO item_price FROM Dessert WHERE DessertID = item_id_param;
        INSERT INTO OrderDessert (OrderID, DessertID, Quantity, ItemPrice)
        VALUES (order_id_param, item_id_param, quantity_param, item_price)
        ON CONFLICT (OrderID, DessertID) DO UPDATE
        SET Quantity = OrderDessert.Quantity + quantity_param;
    ELSE
        RAISE EXCEPTION 'Неправильний тип позиції: %', item_type;
    END IF;

    UPDATE "Order" AS O
    SET TotalAmount = COALESCE(
        (SELECT SUM(OD.Quantity * OD.ItemPrice) FROM OrderDrink OD WHERE OD.OrderID = O.OrderID), 0
    ) + COALESCE(
        (SELECT SUM(O_D.Quantity * O_D.ItemPrice) FROM OrderDessert O_D WHERE O_D.OrderID = O.OrderID), 0
    )
    WHERE O.OrderID = order_id_param;
END;
$$;


--
-- Name: add_new_client(character varying, character varying, character varying, date, character varying, text, numeric); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.add_new_client(_firstname character varying, _lastname character varying, _middlename character varying DEFAULT NULL::character varying, _dob date DEFAULT NULL::date, _phone character varying DEFAULT NULL::character varying, _address text DEFAULT NULL::text, _discount numeric DEFAULT 0) RETURNS integer
    LANGUAGE sql
    AS $$
    INSERT INTO Client (FirstName, LastName, MiddleName, DateOfBirth, PhoneNumber, Address, Discount)
    VALUES (_firstname, _lastname, _middlename, _dob, _phone, _address, _discount)
    RETURNING ClientID;
$$;


--
-- Name: add_new_dessert_order(integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.add_new_dessert_order(employee_id_param integer, client_id_param integer, dessert_id_param integer, quantity_param integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    new_order_id INT;
    item_price NUMERIC(6, 2);
    total_cost NUMERIC(8, 2);
BEGIN
    SELECT Price INTO item_price FROM Dessert WHERE DessertID = dessert_id_param;
    total_cost := item_price * quantity_param;

    INSERT INTO "Order" (EmployeeID, ClientID, TotalAmount)
    VALUES (employee_id_param, client_id_param, total_cost)
    RETURNING OrderID INTO new_order_id;
    
    INSERT INTO OrderDessert (OrderID, DessertID, Quantity, ItemPrice)
    VALUES (new_order_id, dessert_id_param, quantity_param, item_price);
    
    RETURN new_order_id;
END;
$$;


--
-- Name: add_new_drink_order(integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.add_new_drink_order(employee_id_param integer, client_id_param integer, drink_id_param integer, quantity_param integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    new_order_id INT;
    item_price NUMERIC(6, 2);
    total_cost NUMERIC(8, 2);
BEGIN
    SELECT Price INTO item_price FROM Drink WHERE DrinkID = drink_id_param;
    total_cost := item_price * quantity_param;

    INSERT INTO "Order" (EmployeeID, ClientID, TotalAmount)
    VALUES (employee_id_param, client_id_param, total_cost)
    RETURNING OrderID INTO new_order_id;
    
    INSERT INTO OrderDrink (OrderID, DrinkID, Quantity, ItemPrice)
    VALUES (new_order_id, drink_id_param, quantity_param, item_price);
    
    RETURN new_order_id;
END;
$$;


--
-- Name: add_work_schedule(integer, date, time without time zone, time without time zone); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.add_work_schedule(employee_id_to_schedule integer, work_date date, start_time time without time zone, end_time time without time zone) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO WorkSchedule (EmployeeID, WorkDate, StartTime, EndTime)
    VALUES (employee_id_to_schedule, work_date, start_time, end_time);
END;
$$;


--
-- Name: delete_client_by_id(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_client_by_id(client_id_to_delete integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM Client
    WHERE ClientID = client_id_to_delete;
END;
$$;


--
-- Name: delete_dessert_by_id(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_dessert_by_id(dessert_id_to_delete integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM Dessert
    WHERE DessertID = dessert_id_to_delete;
END;
$$;


--
-- Name: delete_drink(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_drink(drink_id_to_delete integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM Drink
    WHERE DrinkID = drink_id_to_delete;
END;
$$;


--
-- Name: delete_order_by_id(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_order_by_id(order_id_to_delete integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM "Order"
    WHERE OrderID = order_id_to_delete;
END;
$$;


--
-- Name: delete_schedule_between_dates(date, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_schedule_between_dates(start_date date, end_date date) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM WorkSchedule
    WHERE WorkDate BETWEEN start_date AND end_date;
END;
$$;


--
-- Name: delete_schedule_by_date(date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.delete_schedule_by_date(date_to_delete date) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM WorkSchedule
    WHERE WorkDate = date_to_delete;
END;
$$;


--
-- Name: dismiss_barista(character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.dismiss_barista(employee_last_name character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM Employee
    WHERE LastName = employee_last_name
      AND PositionID = (SELECT PositionID FROM Position WHERE Title_UA = 'Бариста');
END;
$$;


--
-- Name: dismiss_waiter(character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.dismiss_waiter(employee_last_name character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM Employee
    WHERE LastName = employee_last_name
      AND PositionID = (SELECT PositionID FROM Position WHERE Title_UA = 'Офіціант');
END;
$$;


--
-- Name: get_all_baristas(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_all_baristas() RETURNS TABLE(employee_id integer, first_name character varying, last_name character varying, phone_number character varying, address text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT E.EmployeeID, E.FirstName, E.LastName, E.PhoneNumber, E.Address
    FROM Employee E
    JOIN Position P ON E.PositionID = P.PositionID
    WHERE P.Title_UA = 'Бариста';
END;
$$;


--
-- Name: get_all_desserts(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_all_desserts() RETURNS TABLE(dessert_id integer, name_ua character varying, name_en character varying, price numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT D.DessertID, D.Name_UA, D.Name_EN, D.Price
    FROM Dessert D;
END;
$$;


--
-- Name: get_all_drinks(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_all_drinks() RETURNS TABLE(drink_id integer, name_ua character varying, name_en character varying, price numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT D.DrinkID, D.Name_UA, D.Name_EN, D.Price
    FROM Drink D;
END;
$$;


--
-- Name: get_all_waiters(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_all_waiters() RETURNS TABLE(employee_id integer, first_name character varying, last_name character varying, phone_number character varying, address text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT E.EmployeeID, E.FirstName, E.LastName, E.PhoneNumber, E.Address
    FROM Employee E
    JOIN Position P ON E.PositionID = P.PositionID
    WHERE P.Title_UA = 'Офіціант';
END;
$$;


--
-- Name: get_orders_by_client(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_orders_by_client(client_id_param integer) RETURNS TABLE(order_id integer, order_timestamp timestamp without time zone, employee_id integer, total_amount numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT O.OrderID, O.OrderTimestamp, O.EmployeeID, O.TotalAmount
    FROM "Order" O
    WHERE O.ClientID = client_id_param
    ORDER BY O.OrderTimestamp DESC;
END;
$$;


--
-- Name: get_orders_by_dessert(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_orders_by_dessert(dessert_id_param integer) RETURNS TABLE(order_id integer, order_timestamp timestamp without time zone, total_amount numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT O.OrderID, O.OrderTimestamp, O.TotalAmount
    FROM "Order" O
    JOIN OrderDessert OD ON O.OrderID = OD.OrderID
    WHERE OD.DessertID = dessert_id_param
    ORDER BY O.OrderTimestamp DESC;
END;
$$;


--
-- Name: get_orders_by_waiter(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_orders_by_waiter(employee_id_param integer) RETURNS TABLE(order_id integer, order_timestamp timestamp without time zone, client_id integer, total_amount numeric)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT O.OrderID, O.OrderTimestamp, O.ClientID, O.TotalAmount
    FROM "Order" O
    WHERE O.EmployeeID = employee_id_param
    ORDER BY O.OrderTimestamp DESC;
END;
$$;


--
-- Name: get_schedule_by_date(date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.get_schedule_by_date(schedule_date date) RETURNS TABLE(employee_id integer, first_name character varying, last_name character varying, position_title character varying, start_time time without time zone, end_time time without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT
        E.EmployeeID, E.FirstName, E.LastName, P.Title_UA, W.StartTime, W.EndTime
    FROM WorkSchedule W
    JOIN Employee E ON W.EmployeeID = E.EmployeeID
    JOIN Position P ON E.PositionID = P.PositionID
    WHERE W.WorkDate = schedule_date;
END;
$$;


--
-- Name: update_barista_phone(character varying, character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_barista_phone(barista_last_name character varying, new_phone_number character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    barista_pos_id INT;
BEGIN
    SELECT PositionID INTO barista_pos_id FROM Position WHERE Title_UA = 'Бариста';

    UPDATE Employee
    SET PhoneNumber = new_phone_number
    WHERE LastName = barista_last_name
      AND PositionID = barista_pos_id;
END;
$$;


--
-- Name: update_client_discount(integer, numeric); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_client_discount(client_id_to_update integer, new_discount_percent numeric) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE Client
    SET Discount = new_discount_percent
    WHERE ClientID = client_id_to_update;
END;
$$;


--
-- Name: update_dessert_name(integer, character varying, character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_dessert_name(dessert_id_to_update integer, new_name_ua character varying, new_name_en character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE Dessert
    SET Name_UA = new_name_ua,
        Name_EN = new_name_en
    WHERE DessertID = dessert_id_to_update;
END;
$$;


--
-- Name: update_drink_name(integer, character varying, character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_drink_name(drink_id_to_update integer, new_name_ua character varying, new_name_en character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE Drink
    SET Name_UA = new_name_ua,
        Name_EN = new_name_en
    WHERE DrinkID = drink_id_to_update;
END;
$$;


--
-- Name: update_drink_price(integer, numeric); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_drink_price(drink_id_to_update integer, new_price numeric) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE Drink
    SET Price = new_price
    WHERE DrinkID = drink_id_to_update;
END;
$$;


--
-- Name: update_pastry_chef_address(character varying, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_pastry_chef_address(chef_last_name character varying, new_address text) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    chef_pos_id INT;
BEGIN
    SELECT PositionID INTO chef_pos_id FROM Position WHERE Title_UA = 'Кондитер';

    UPDATE Employee
    SET Address = new_address
    WHERE LastName = chef_last_name
      AND PositionID = chef_pos_id;
END;
$$;


--
-- Name: update_schedule_by_date(integer, date, time without time zone, time without time zone); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_schedule_by_date(employee_id_param integer, work_date_param date, new_start_time time without time zone, new_end_time time without time zone) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE WorkSchedule
    SET StartTime = new_start_time,
        EndTime = new_end_time
    WHERE EmployeeID = employee_id_param AND WorkDate = work_date_param;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Order" (
    orderid integer NOT NULL,
    ordertimestamp timestamp without time zone DEFAULT now() NOT NULL,
    employeeid integer,
    clientid integer,
    totalamount numeric(8,2) NOT NULL
);


--
-- Name: Order_orderid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."Order_orderid_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: Order_orderid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."Order_orderid_seq" OWNED BY public."Order".orderid;


--
-- Name: client; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.client (
    clientid integer NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    middlename character varying(50),
    dateofbirth date,
    phonenumber character varying(20),
    address text,
    discount numeric(5,2) DEFAULT 0 NOT NULL,
    CONSTRAINT client_discount_check CHECK (((discount >= (0)::numeric) AND (discount <= (100)::numeric)))
);


--
-- Name: client_clientid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.client_clientid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: client_clientid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.client_clientid_seq OWNED BY public.client.clientid;


--
-- Name: dessert; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dessert (
    dessertid integer NOT NULL,
    name_ua character varying(100) NOT NULL,
    name_en character varying(100) NOT NULL,
    price numeric(6,2) NOT NULL,
    CONSTRAINT dessert_price_check CHECK ((price >= (0)::numeric))
);


--
-- Name: dessert_dessertid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dessert_dessertid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dessert_dessertid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.dessert_dessertid_seq OWNED BY public.dessert.dessertid;


--
-- Name: drink; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.drink (
    drinkid integer NOT NULL,
    name_ua character varying(100) NOT NULL,
    name_en character varying(100) NOT NULL,
    price numeric(6,2) NOT NULL,
    CONSTRAINT drink_price_check CHECK ((price >= (0)::numeric))
);


--
-- Name: drink_drinkid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.drink_drinkid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: drink_drinkid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.drink_drinkid_seq OWNED BY public.drink.drinkid;


--
-- Name: employee; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employee (
    employeeid integer NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    middlename character varying(50),
    phonenumber character varying(20) NOT NULL,
    address text,
    positionid integer NOT NULL
);


--
-- Name: employee_employeeid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.employee_employeeid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: employee_employeeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.employee_employeeid_seq OWNED BY public.employee.employeeid;


--
-- Name: orderdessert; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orderdessert (
    orderdessertid integer NOT NULL,
    orderid integer NOT NULL,
    dessertid integer NOT NULL,
    quantity integer NOT NULL,
    itemprice numeric(6,2) NOT NULL,
    CONSTRAINT orderdessert_quantity_check CHECK ((quantity > 0))
);


--
-- Name: orderdessert_orderdessertid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orderdessert_orderdessertid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: orderdessert_orderdessertid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orderdessert_orderdessertid_seq OWNED BY public.orderdessert.orderdessertid;


--
-- Name: orderdrink; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orderdrink (
    orderdrinkid integer NOT NULL,
    orderid integer NOT NULL,
    drinkid integer NOT NULL,
    quantity integer NOT NULL,
    itemprice numeric(6,2) NOT NULL,
    CONSTRAINT orderdrink_quantity_check CHECK ((quantity > 0))
);


--
-- Name: orderdrink_orderdrinkid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orderdrink_orderdrinkid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: orderdrink_orderdrinkid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orderdrink_orderdrinkid_seq OWNED BY public.orderdrink.orderdrinkid;


--
-- Name: position; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."position" (
    positionid integer NOT NULL,
    title_ua character varying(50) NOT NULL,
    title_en character varying(50) NOT NULL
);


--
-- Name: position_positionid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.position_positionid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: position_positionid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.position_positionid_seq OWNED BY public."position".positionid;


--
-- Name: workschedule; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.workschedule (
    scheduleid integer NOT NULL,
    employeeid integer NOT NULL,
    workdate date NOT NULL,
    starttime time without time zone NOT NULL,
    endtime time without time zone NOT NULL
);


--
-- Name: workschedule_scheduleid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.workschedule_scheduleid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: workschedule_scheduleid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.workschedule_scheduleid_seq OWNED BY public.workschedule.scheduleid;


--
-- Name: Order orderid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Order" ALTER COLUMN orderid SET DEFAULT nextval('public."Order_orderid_seq"'::regclass);


--
-- Name: client clientid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client ALTER COLUMN clientid SET DEFAULT nextval('public.client_clientid_seq'::regclass);


--
-- Name: dessert dessertid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dessert ALTER COLUMN dessertid SET DEFAULT nextval('public.dessert_dessertid_seq'::regclass);


--
-- Name: drink drinkid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drink ALTER COLUMN drinkid SET DEFAULT nextval('public.drink_drinkid_seq'::regclass);


--
-- Name: employee employeeid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee ALTER COLUMN employeeid SET DEFAULT nextval('public.employee_employeeid_seq'::regclass);


--
-- Name: orderdessert orderdessertid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdessert ALTER COLUMN orderdessertid SET DEFAULT nextval('public.orderdessert_orderdessertid_seq'::regclass);


--
-- Name: orderdrink orderdrinkid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdrink ALTER COLUMN orderdrinkid SET DEFAULT nextval('public.orderdrink_orderdrinkid_seq'::regclass);


--
-- Name: position positionid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."position" ALTER COLUMN positionid SET DEFAULT nextval('public.position_positionid_seq'::regclass);


--
-- Name: workschedule scheduleid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workschedule ALTER COLUMN scheduleid SET DEFAULT nextval('public.workschedule_scheduleid_seq'::regclass);


--
-- Name: Order Order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Order"
    ADD CONSTRAINT "Order_pkey" PRIMARY KEY (orderid);


--
-- Name: client client_phonenumber_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_phonenumber_key UNIQUE (phonenumber);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (clientid);


--
-- Name: dessert dessert_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dessert
    ADD CONSTRAINT dessert_pkey PRIMARY KEY (dessertid);


--
-- Name: drink drink_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drink
    ADD CONSTRAINT drink_pkey PRIMARY KEY (drinkid);


--
-- Name: employee employee_phonenumber_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_phonenumber_key UNIQUE (phonenumber);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employeeid);


--
-- Name: orderdessert orderdessert_orderid_dessertid_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdessert
    ADD CONSTRAINT orderdessert_orderid_dessertid_key UNIQUE (orderid, dessertid);


--
-- Name: orderdessert orderdessert_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdessert
    ADD CONSTRAINT orderdessert_pkey PRIMARY KEY (orderdessertid);


--
-- Name: orderdrink orderdrink_orderid_drinkid_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdrink
    ADD CONSTRAINT orderdrink_orderid_drinkid_key UNIQUE (orderid, drinkid);


--
-- Name: orderdrink orderdrink_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdrink
    ADD CONSTRAINT orderdrink_pkey PRIMARY KEY (orderdrinkid);


--
-- Name: position position_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (positionid);


--
-- Name: position position_title_en_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_title_en_key UNIQUE (title_en);


--
-- Name: position position_title_ua_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_title_ua_key UNIQUE (title_ua);


--
-- Name: workschedule workschedule_employeeid_workdate_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workschedule
    ADD CONSTRAINT workschedule_employeeid_workdate_key UNIQUE (employeeid, workdate);


--
-- Name: workschedule workschedule_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workschedule
    ADD CONSTRAINT workschedule_pkey PRIMARY KEY (scheduleid);


--
-- Name: Order Order_clientid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Order"
    ADD CONSTRAINT "Order_clientid_fkey" FOREIGN KEY (clientid) REFERENCES public.client(clientid);


--
-- Name: Order Order_employeeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Order"
    ADD CONSTRAINT "Order_employeeid_fkey" FOREIGN KEY (employeeid) REFERENCES public.employee(employeeid);


--
-- Name: employee employee_positionid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_positionid_fkey FOREIGN KEY (positionid) REFERENCES public."position"(positionid);


--
-- Name: orderdessert orderdessert_dessertid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdessert
    ADD CONSTRAINT orderdessert_dessertid_fkey FOREIGN KEY (dessertid) REFERENCES public.dessert(dessertid);


--
-- Name: orderdessert orderdessert_orderid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdessert
    ADD CONSTRAINT orderdessert_orderid_fkey FOREIGN KEY (orderid) REFERENCES public."Order"(orderid) ON DELETE CASCADE;


--
-- Name: orderdrink orderdrink_drinkid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdrink
    ADD CONSTRAINT orderdrink_drinkid_fkey FOREIGN KEY (drinkid) REFERENCES public.drink(drinkid);


--
-- Name: orderdrink orderdrink_orderid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orderdrink
    ADD CONSTRAINT orderdrink_orderid_fkey FOREIGN KEY (orderid) REFERENCES public."Order"(orderid) ON DELETE CASCADE;


--
-- Name: workschedule workschedule_employeeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workschedule
    ADD CONSTRAINT workschedule_employeeid_fkey FOREIGN KEY (employeeid) REFERENCES public.employee(employeeid);


--
-- PostgreSQL database dump complete
--

