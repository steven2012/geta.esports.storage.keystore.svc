package co.io.geta.platform.crosscutting.constants;

public class GetaConstants {

	private GetaConstants() {
	}

	// Properties key
	public static final String NAME_TABLE_RULES = "${name.table.storage.encrypted}";

	// App parameters
	public static final String AES_ALGORITHM = "AES/GCM/NoPadding";
	public static final String SHA1 = "SHA-1";
	public static final String AES = "AES";
	public static final String SEED_ENCRYPTOR = "${seed-encryptor}";

	// Key jsons navigates
	public static final String KEY_JSON_RULES = "Data";
	public static final String KEY_ROOT_JSON = "KeyRules";
	public static final String NAME_PROPERTIES = "PartitionKey";
	public static final String VALUE_PARTITION_KEY = "Rule";

	/// URL API STORAGE
	public static final String ROOT_API_STORAGE = "/storage";
	public static final String RULES = "/rules";
	public static final String ALL_RULES = "/all-rules";

	// Conection String
	public static final String CONECTING_STRING_AZURE_STORAGE = "${spring.data.azuredb.conecttion}";

	// Message
	public static final String KEY_NOT_FOUND = "not.found.key";
	public static final String BAD_REQUEST = "bad.request";
	public static final String PARAM_NOT_NULL = "param.not.null";
	public static final String PARAM_NOT_EMPTY = "param.not.empty";
	public static final String MAPPING_ERROR = "error.maping.object";

	public static final String MAPPING_ERROR_MAP = "object.error.maping.map";

	// Exception Messages
	public static final String REFERENCE_TABLE_EXCEPTION = "cloud.table.exception";
	public static final String TABLE_EXECUTE_EXCEPTION = "clod.table.execute.exception";

	public static final String URI_KEY_VAUL = "https://kv-getaclub-dev.vault.azure.net/";

}
