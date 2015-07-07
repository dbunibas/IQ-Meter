--> ------------ DAOClover: Vincoli del Driver -------------- <--

Funzioni (box) riconosciute dal driver:
PARTITION, REFORMAT, EXT_HASH_JOIN, DUDUP, EXT_SORT, XML_EXTRACT, EXT_XML_WRITER, EXT_FILTER, SIMPLECOPY, MERGE, AGGREGATE


Vincoli:
--# Gli schema utilizzati nei grafi (per writer e reader) devono avere lo stesso nome di quelli utilizzati nello scenario di valuzione IQMeter(Es. se ho specificato in IQMeter lo schema source.xsd, nel grafo di clover dovrà chiamarsi source.xsd);

--# Non sono supportate "phase" multiple;

--# Nel writer e reader devono essere inclusi (spuntati) anche gli elementi root (es. Source, Target);

--# L'ordine di apparizione (le coordinate x) viene considerato per il layout dell'effort graph;

--# Tutti i metadati devono essere creati dagli schema ed il file deve essere nominato schema_Elemento (Es: atomicS.xsd_Contact.fmt) oppure con il path completo (Es: Target.Contact);

--# Tutti i metadati devono trovarsi all'interno della cartella di progetto di Clover, nella directory "meta"; 

--# Se abbiamo necessità di metadati intermedi allora i file devono essere nominati con la dicitura _Flat e chiamarsi come l'elemento radice (Es: Contact_Flat.fmt dove Contact e elemento radice del file);

--# Affinchè venga mappato in maniera corretta è opportuno che gli elementi nel writer provengano da metadata dello schema target.

--# Blocco Partition: sono riconosciute partizioni definite per chiave oppure attraverso condizione esplicita dichiarata mediante istruzione "if" all'interno di getOutputPort(), in questo caso è preferibile evitare istruzioni "else" in modo da evitare la generazione di eventuali annotazione superflue 
Es:
function integer getOutputPort() {
	if($in.0.type == "primary"){
		return 0;
	}
  return 1;
} 


Note: 
--# Se alcuni dei vincoli non sono rispettati allora il mapping avviene in modo scorretto;

--# Le funzioni non presenti tra quelle riconosciute dal driver, vengono comunque caricate ma non viene valutato il contributo in termini di mapping tra nodi schema. Ciò vuol dire che non vengono generati archi tra nodi schema, ma verrà generato solo il nodo box con i rispettivi archi verso glia altri box collegati ad esso.
