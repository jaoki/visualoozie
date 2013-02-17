visualoozie
===========

visualoozie

http://localhost/visualoozie/visualize_xml

How to Deploy to cloudbees
===========

mvn clean package 
bees app:deploy -a visualoozie/alpha target/visualoozie.war

