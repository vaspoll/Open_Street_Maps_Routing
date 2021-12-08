Για την εκτέλεση των προγραμμάτων μας πρέπει να ακολουθηθούν τα ακόλουθα βήματα:

1) Εκτέλεση σε τερματικό των εντολών προεπεξεργασίας δεδομένων (csv) μέσω Java για παραγωγή γνώσης Prolog
- javac preprocessing1.java
-java preprocessing1
- javac preprocessing2.java
-java preprocessing2
- javac preprocessing3.java
-java preprocessing3
- javac preprocessing4.java
-java preprocessing4

2) Τον εμπλουτισμό της Βάσης Γνώσης prolog με την εκτέλεση των παρακάτω δύο συνεδριών Prolog:
α) 1η συνεδρία:
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
β) 2η συνεδρία:
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

3) Εκτέλεση σε τερματικό του βασικού προγράμματος Java:
javac A_star.java
java A_star

Εισάγετε το k (π.χ. 5) με τη σημασιολογία που περιγράφεται στην εκφώνηση.
Τα αποτελέσματα εμφανίζονται στο τερματικό και εξάγονται σε 4 αρχεία .kml όπως περιγράφουμε στην αναφορά.