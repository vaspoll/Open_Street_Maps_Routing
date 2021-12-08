Execution instructions

1) Download from open street maps the osm file corresponding to the area of interest.
   Execute python script osm_to_csv.py to parse the osm file into csv format

2) 
- javac preprocessing1.java
-java preprocessing1
- javac preprocessing2.java
-java preprocessing2
- javac preprocessing3.java
-java preprocessing3
- javac preprocessing4.java
-java preprocessing4

3) Run in Prolog the following:
consult(node_id).
consult(nodeX).
consult(nodeY).
consult(increasing_number_line_id).
consult(line_one_way).
consult(is_road).
consult(no_light).
consult(has_toll).
consult(incline).
consult(speed_limit).
consult(traffic).
consult(client).
consult(taxi).
consult(rules).
open('next.pl', write, ID),(is_next(INC_NUM1, INC_NUM2, J2, 20.0, DT, DIST), write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)), write(ID, '.'), nl(ID), fail; close(ID)).
open('adequate.pl', write, ID), (taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail; close(ID)).
consult(node_id).
consult(nodeX).
consult(nodeY).
consult(increasing_number_line_id).
consult(line_one_way).
consult(is_road).
consult(no_light).
consult(has_toll).
consult(incline).
consult(speed_limit).
consult(traffic).
consult(client_custom).
consult(taxi_custom).
consult(rules).
open('next_custom.pl', write, ID),(is_next(INC_NUM1, INC_NUM2, J2, 20.0, DT, DIST), write(ID, next(INC_NUM1, INC_NUM2, J2, DT, DIST)), write(ID, '.'), nl(ID), fail; close(ID)).
open('adequate_custom.pl', write, ID), (taxi_adequacy(TAXI_ID, INC_NUM), write(ID, adequate(TAXI_ID, INC_NUM)), write(ID, '.'), nl(ID), fail; close(ID)).

4) Run the main function that gives the optimal distances between client points and starting points
javac dijkstra.java
java dijkstra

5) The above distances form a complete graph. Copy the edge weights into ortools.py and execute it to solve the routing problem and obtain the optimal route
