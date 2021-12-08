/*node_id(INC_NUM, N_ID),
nodeX(N_ID, X),
nodeY(N_ID, Y),

increasing_number_line_id(INC_NUM, L_ID),
line_one_way(L_ID, DIRECTION),
is_road(L_ID),
no_light(L_ID),
has_toll(L_ID),
incline(L_ID, INCLINATION),
speed_limit(L_ID, SPEED_LIMIT),
traffic(L_ID, BITMASK),*/

increasing_number_coordinates(INC_NUM, X, Y) :-
	node_id(INC_NUM, N_ID),
	nodeX(N_ID, X),
	nodeY(N_ID, Y).

max_speed(L_ID, TIME, U) :-
    ((\+ incline(L_ID, _))-> (INCLINATION is 5)    
    ;    incline(L_ID, INCLINATION)
    ),
	((\+ speed_limit(L_ID, _))-> (SPEED_LIMIT is 30)    
    ;    speed_limit(L_ID, SPEED_LIMIT)
    ),
    INT_TIME_TMP is floor(TIME),
	(   INT_TIME_TMP = 0 ->
	    INT_TIME = 24
	;   INT_TIME = INT_TIME_TMP
	),
    TIMEZONE is (INT_TIME - 1) // 2,
    TIME_BIT is 3 ^ TIMEZONE,
	((\+ traffic(L_ID, _))-> (BITMASK is 0)    
    ;    traffic(L_ID, BITMASK)
    ),
    HAS_TRAFFIC is (BITMASK // TIME_BIT) mod 3,
	( HAS_TRAFFIC =:= 0 ->
	    U1 is 100
	;   HAS_TRAFFIC =:= 1 ->
	    U1 is 30
	;   U1 is 5
	),
	U2 is SPEED_LIMIT,
	(   25=<abs(INCLINATION) ->
	    U3 is 15
	;   (abs(INCLINATION)<25, 20=<abs(INCLINATION)) ->
	    U3 is 30
	;   U3 is 100
	),
	U is min(min(U1, U2), U3).
	
	
next_or_prev_num(A, B) :- B is A + 1.
next_or_prev_num(A, B) :- B is A - 1.

not_scary_sys(L_ID, _) :- (\+(no_light(L_ID))).
not_scary_sys(_, TIME) :-
    TIME > 6.0,
    TIME < 21.0.

not_scary(L_ID, TIME) :- not_scary_sys(L_ID, TIME), !.


is_next(INC_NUM1, INC_NUM2, J2, TIME, 0.0, 0.0) :-
    node_id(INC_NUM1, N_ID),
    node_id(INC_NUM2, N_ID),
    \+ (INC_NUM2 =:= INC_NUM1),
    increasing_number_line_id(INC_NUM2, L_ID2),
    is_road(L_ID2),
    not_scary(L_ID2, TIME),
    ( has_toll(L_ID2) ->
      J2 is 2.0
    ; J2 is 0.0000001
    ).

is_next(INC_NUM1, INC_NUM2, J2, TIME, DT, DIST) :-
    node_id(INC_NUM1, N_ID1),
    increasing_number_line_id(INC_NUM1, L_ID),
    ((\+incline(L_ID, _))-> (INCLINATION is 2.5)    
    ;    incline(L_ID, INCLINATION)
    ),
    ( line_one_way(L_ID, 1) ->
      INC_NUM2 is INC_NUM1 + 1
    ; line_one_way(L_ID, -1) ->
	  INC_NUM2 is INC_NUM1 - 1
	; next_or_prev_num(INC_NUM1, INC_NUM2)
    ),
    node_id(INC_NUM2, N_ID2),
    increasing_number_line_id(INC_NUM2, L_ID),
    nodeX(N_ID1, X1),
    nodeY(N_ID1, Y1),
    nodeX(N_ID2, X2),
    nodeY(N_ID2, Y2),
    calc_dist(X1, Y1, X2, Y2, H_DIST),
	DIST is H_DIST,
    max_speed(L_ID, TIME, U),
    DURATION is (DIST / U),
	DT is DURATION,
    ( TIME > 5.0 ->
      J2 is 0.65 * DIST + 10.46 * DURATION
    ; J2 is 1.14 * DIST + 10.46 * DURATION
    ).
	

	

/*client(X, Y, X_DEST, Y_DEST, TIME, PERSONS, LANGUAGE, LUGGAGE)
taxi(X, Y, ID, AVAILABLE, CAPACITY, LANGUAGES, RATING, LONG_DISTANCE, TYPE)
next: open('next.pl', write, ID),(is_next(INC_NUM1, INC_NUM2, J2, 20.0, DT, DIST), write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)), write(ID, '.'), nl(ID), fail; close(ID)).
taxi_adequacy: open('adequate.pl', write, ID), (taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail; close(ID)).
next_custom: open('next_custom.pl', write, ID),(is_next(INC_NUM1, INC_NUM2, J2, 03.25, DT, DIST), write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)), write(ID, '.'), nl(ID), fail; close(ID)).
taxi_adequacy_custom: open('adequate_custom.pl', write, ID), (taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail; close(ID)).*/

/*

next: open('next.pl', write, ID),is_next(INC_NUM1, INC_NUM2, J2, 20.0, DT, DIST),write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)),write(ID, '.'), nl(ID), fail, close(ID).
taxi_adequacy: open('adequate.pl', write, ID), taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail, close(ID).
next_custom: open('next_custom.pl', write, ID), is_next(INC_NUM1, INC_NUM2, J2, 03.25, DT, DIST), write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)), write(ID, '.'), nl(ID), fail, close(ID).
taxi_adequacy_custom: open('adequate_custom.pl', write, ID), taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail, close(ID).


*/

max_luggage(subcompact, 2).
max_luggage(compact, 3).
max_luggage(large, 5).

calc_dist(X1, Y1, X2, Y2, DIST) :-
    CONV is 3.14159265359 / 180,
    DX is X1 - X2,
    DY is Y1 - Y2,
    R is 6371,
    A is ((sin(CONV * (DY /2))) ^ 2) + ((cos(CONV * Y1) * cos(CONV * Y2)) * ((sin(CONV * (DX / 2))) ^ 2)),
    C is 2 * atan2(sqrt(A), sqrt(1 - A)),
    DIST is R * C.

/*
find_closest([], MIN, MINI, MIN, MINI).
find_closest([distance_duo(DIST, N_ID)|REST], SOFAR_MIN, SOFAR_MINI, MIN, MINI):-
	(DIST < SOFAR_MIN -> find_closest(REST, DIST, N_ID, MIN, MINI);
	find_closest(REST, SOFAR_MIN, SOFAR_MINI, MIN, MINI)).
*/

find_closest([], 100000, -1).
find_closest([distance_duo(DIST, N_ID)|REST], MIN, MINI) :-
	find_closest(REST, H_MIN, H_MINI),
	( DIST < H_MIN -> (MINI = N_ID, MIN = DIST);
	  (MINI = H_MINI, MIN = H_MIN)).

goal(DIST, N_ID, X1, Y1):-
	node_id(INC_NUM, N_ID),
	increasing_number_line_id(INC_NUM, L_ID),
	is_road(L_ID),
    nodeX(N_ID, X),
    nodeY(N_ID, Y),
    calc_dist(X1, Y1, X, Y, DIST).

closest_node(X, Y, INC_NUM):-
    findall( distance_duo(DIST, N_ID), goal(DIST, N_ID, X, Y), SOLUTIONS),
    find_closest(SOLUTIONS, _, CLOSEST_N_ID),
    node_id(INC_NUM, CLOSEST_N_ID),!.

dist_from_client(INC_NUM, DIST) :-
    client(X1, Y1, _, _, _, _, _, _),
    node_id(INC_NUM, N_ID),
    nodeX(N_ID, X2),
    nodeY(N_ID, Y2),
	calc_dist(X1, Y1, X2, Y2, DIST).

dist_from_dest(INC_NUM, DIST) :-
    client(_, _, X1, Y1, _, _, _, _),
    node_id(INC_NUM, N_ID),
    nodeX(N_ID, X2),
    nodeY(N_ID, Y2),
	calc_dist(X1, Y1, X2, Y2, DIST).

taxi_adequacy(TAXI_ID, INC_NUM) :-
    taxi(X_TAXI, Y_TAXI, TAXI_ID, yes, CAPACITY, LANGUAGES, _, LONG_DISTANCE, TYPE),
	closest_node(X_TAXI, Y_TAXI, INC_NUM),
	dist_from_client(INC_NUM, DIST_FROM_CLIENT),
	dist_from_dest(INC_NUM, DIST_FROM_DEST),
    client(_, _, _, _, _, PERSONS, CL_LANGUAGE, CL_LUGGAGE),
    PERSONS < CAPACITY,
    member(CL_LANGUAGE, LANGUAGES),
    ( LONG_DISTANCE = no ->
       ( DIST_FROM_DEST < 50,
         DIST_FROM_CLIENT < 50)
	   ; 1 = 1
    ),
    max_luggage(TYPE, MAX_LUGGAGE),
    MAX_LUGGAGE >= CL_LUGGAGE.
    
taxi_rank(TAXI_ID, WAITING_TIME, RANK) :-
    taxi(_, _, TAXI_ID, _, _, _, RATING, _, _),
    RANK is (RATING / WAITING_TIME).
	
