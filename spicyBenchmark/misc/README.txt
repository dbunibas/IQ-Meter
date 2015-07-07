----------------------------
Distribution
----------------------------
spicybenchmark-java
|--- javadoc (source code documentation)
|--- lib (lib directory)
|--- misc (other files)
      |--- resources (sample data)
|--- src (source code directory)
|--- test (test code directory)

----------------------------
Usage:
----------------------------

spicyBench.bat <configuration file name>

where:

<configuration file name> is a .properties file containing the scenario configuration

For instance, launching the above command on the files 

statdb.xsd 
statDB-expectedInstance.xml 
statDB-translatedInstance.xml

contained in the directory "./resources/datasources" we get the following output:

INFO: Precision: 1.0
INFO: Recall: 1.0
INFO: F-Measure: 1.0

This is a trivial example in which the expected instance and translated instance are exactly the same

The directory "./resources/datasources" contains other test experiments that can be used.

-----------------------------------------------------------------------------------------------------------
Notes on the id generation algorithm
---------------------------------------------------------
In the following we describe the id generation algorithm by means of an example.

If we assume the following target schema: 

----------- Target Schema ------------------
statDB : SetNode
    cityStat : SequenceNode
        city : AttributeNode
        organizations : SetNode
            organization : SequenceNode
                companyName : AttributeNode
                fundings : SetNode
                    fund : SequenceNode
                        principalInvestigator : AttributeNode
                        financialIdRef : AttributeNode
        financials : SetNode
            financial : SequenceNode
                financialId : AttributeNode
                amount : AttributeNode
                project : AttributeNode
                year : AttributeNode


and the following target instance:
				
<statDB>
    <cityStat>
        <city>New York</city>
        <organizations>
            <organization>
                <companyId>1</companyId>
                <companyName>IBM</companyName>
                <fundings>
                    <fund>
                        <principalInvestigator>Frank Miller</principalInvestigator>
                        <financialIdRef>101</financialIdRef>
                    </fund>
                    <fund>...</fund>
                </fundings>
            </organization>
        </organizations>
        <financials>
            ...
        </financials>
    </cityStat>
    <cityStat>
		...
    </cityStat>
</statDB>

we are able to create two kinds of ids: global id and local id.

The local id is generated for every tuple node by concatenating the values of its children nodes (with dash as a separator).
The global id is recursively generated for every tuple node as follows:
- if its father node is a tuple node then we append its local id to the local id of its father
- if its father node is all but a tuple node then we append its local id to the name of its father

For instance, the tuple "cityStat" will have the following ids:
Local ID: New York
Global ID: statDB-cityStat(New York)

Similarly, the tuple "organization" will have the following ids:
Local ID: 1-IBM
Global ID: statDB-cityStat(New York)-organizations-organization(1-IBM)

Finally, the tuple "fund" will have the following ids:
Local ID: 101-Frank Miller
Global ID: statDB-cityStat(New York)-organizations-organization(1-IBM)-fundings-fund(101-Frank Miller)

Once the ids have been generated for the two instances (translated and expected instances), we compare the global ids of the tuple nodes found in such instances and we compute precision and recall, using the standard formulas.

If one of the two instances contains duplicates (examples can be found in "./resources/dataSources" directory), we simply consider the first pair of matching tuples and discard the other ones. The values of precision and recall will be decreased accordingly.

If we run spicyBench.bat using the following configuration:

statDB.xsd 
statDB-expectedInstance.xml
statDB-translatedInstanceDuplicates.xml

we get the following measures:

INFO: Precision: 0.9333333333333333
INFO: Recall: 1.0
INFO: F-Measure: 0.9655172413793104

If we instead run spicyBench.bat using the following configuration:

statDB.xsd 
statDB-expectedInstanceDuplicates.xml
statDB-translatedInstance.xml

we get the values of precision and recall as follows:

INFO: Precision: 1.0
INFO: Recall: 0.9333333333333333
INFO: F-Measure: 0.9655172413793104

