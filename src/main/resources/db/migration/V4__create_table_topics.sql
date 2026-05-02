CREATE TABLE topics (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        subject_id UUID NOT NULL,
                        name VARCHAR(150) NOT NULL,
                        description TEXT,
                        estimated_hours INTEGER,
                        difficulty VARCHAR(20) NOT NULL,
                        completed BOOLEAN NOT NULL DEFAULT FALSE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_topics_subject
                            FOREIGN KEY (subject_id)
                                REFERENCES subjects(id)
                                ON DELETE CASCADE
);