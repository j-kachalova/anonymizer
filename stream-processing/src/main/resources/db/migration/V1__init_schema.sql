-- V1__initial_schema.sql
-- Initial schema for Data Anonymization Service (with strategy order support)

-- RuleSet table
CREATE TABLE rule_set (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FieldRule table (with order_index for sequential strategy application)
CREATE TABLE field_rule (
                            id SERIAL PRIMARY KEY,
                            rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                            field_name VARCHAR(255) NOT NULL,
                            strategy VARCHAR(100) NOT NULL,
                            params_json TEXT,
                            order_index INTEGER DEFAULT 0
);

-- IdentifierMapping table (для восстановления по IdReplacementStrategy)
CREATE TABLE identifier_mapping (
                                    id SERIAL PRIMARY KEY,
                                    field_name VARCHAR(255) NOT NULL,
                                    original_value_hash VARCHAR(512) NOT NULL UNIQUE,
                                    identifier_value VARCHAR(255) NOT NULL,
                                    rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ShuffleHistory table (для восстановления по ShufflingStrategy)
CREATE TABLE shuffle_history (
                                 id SERIAL PRIMARY KEY,
                                 field_name VARCHAR(255) NOT NULL,
                                 rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                                 shuffle_seed BIGINT NOT NULL,
                                 original_order_json TEXT NOT NULL,
                                 shuffled_order_json TEXT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- DecompositionMapping table (для восстановления по DecompositionStrategy)
CREATE TABLE decomposition_mapping (
                                       id SERIAL PRIMARY KEY,
                                       field_name VARCHAR(255) NOT NULL,
                                       rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                                       decomposition_part_name VARCHAR(255) NOT NULL,
                                       part_value TEXT NOT NULL,
                                       subject_key VARCHAR(255),
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AnonymizedData table (храним обезличенные данные)
CREATE TABLE anonymized_data (
                                 id SERIAL PRIMARY KEY,
                                 rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                                 data_json TEXT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- OriginalData table (опционально храним исходные данные — для восстановления/аудита)
CREATE TABLE original_data (
                               id SERIAL PRIMARY KEY,
                               rule_set_id INTEGER REFERENCES rule_set(id) ON DELETE CASCADE,
                               data_json TEXT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PersonDictionary table (для DictionaryReplacementStrategy)
CREATE TABLE person_dictionary (
                                   id SERIAL PRIMARY KEY,
                                   last_name VARCHAR(100) NOT NULL,
                                   first_name VARCHAR(100) NOT NULL,
                                   patronymic VARCHAR(100) NOT NULL,
                                   gender CHAR(1) NOT NULL CHECK (gender IN ('М', 'Ж'))
);

-- Индексы (для ускорения поиска)
CREATE INDEX idx_identifier_mapping_field ON identifier_mapping(field_name);
CREATE INDEX idx_shuffle_history_field ON shuffle_history(field_name);
CREATE INDEX idx_decomposition_mapping_field ON decomposition_mapping(field_name);
CREATE INDEX idx_anonymized_data_rule_set_id ON anonymized_data(rule_set_id);
CREATE INDEX idx_original_data_rule_set_id ON original_data(rule_set_id);
