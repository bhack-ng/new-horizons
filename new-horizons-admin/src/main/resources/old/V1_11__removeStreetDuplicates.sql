--dont use duplicates duplicates (dont delete first (order by id) street with same name)
UPDATE address
       SET street_id=(
       select min(s1.id) from street s1, street s2 where s1.streetName=s2.streetName AND street_id= s2.id
       group by s1.streetName order by min(s1.id)
       );

--delete duplicates (dont delete first (order by id) street with same name)
delete  from street where id  in (
         select distinct s1.id from street s1, street s2 where s1.streetName=s2.streetName AND s1.id != s2.id
 ) AND id not in (
  	select min(s1.id) from street s1, street s2 where s1.streetName=s2.streetName AND s1.id != s2.id group by s1.streetName order by min(s1.id)
 );


