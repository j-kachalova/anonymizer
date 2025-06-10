-- Таблица наборов правил
CREATE TABLE rulesets (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          json_definition JSONB NOT NULL,
                          created_at TIMESTAMP DEFAULT now()
);

-- Таблица соответствий идентификаторов
CREATE TABLE IF NOT EXISTS identifier_mapping (
                                                  id SERIAL PRIMARY KEY,
                                                  field_name VARCHAR(255) NOT NULL,
    original_value TEXT NOT NULL,
    identifier TEXT NOT NULL,
    UNIQUE (field_name, original_value)
    );


-- Таблица результатов обезличивания (по желанию — можно хранить результаты)
CREATE TABLE anonymization_results (
                                       id UUID PRIMARY KEY,
                                       original_data JSONB NOT NULL,
                                       anonymized_data JSONB NOT NULL,
                                       ruleset_id UUID REFERENCES rulesets(id),
                                       processed_at TIMESTAMP DEFAULT now()
);
