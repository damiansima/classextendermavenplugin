Class Extender Maven Plugin
===========================

The Maven Class Extender plugin alows you to modify java code and make a class extend another given class.

The plug in recieves three parameters:
  * A base directory to work with. Usualy a target folder of your project.
  * A List of package. All the packages listed here will be taken by the plugin in search of classes to exted.
  * A class to extend. The binary name of the class to extend.

The result of the plugin execution is that each class in the listed packages in the base directory will end up extending
the provided class to extend.
It will ofcourse avoid extending classes that are already extending something else.


Usage:
=====
Just add the dependency to your pom.xml file in the build section.
Like so:

<plugin>
  <groupId>org.mule.tools</groupId>
	<artifactId>class-extender-maven</artifactId>
	<version>0.2</version>
	<executions>
	  <execution>
		  <phase>validate</phase>
			<goals>
			  <goal>extend</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <baseDir>HOME/some_project/target/generated-sources/</baseDir>
    <packagesToExtend>
      <param>org.mule.package_1</param>
  		<param>org.mule.package_2</param>
  		...
      <param>org.mule.package_n</param>
    </packagesToExtend>
    <classToExtend>org.mule.AbstractClassToExtend</classToExtend>
  </configuration>