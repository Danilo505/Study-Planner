CREATE TABLE exams (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id UUID NOT NULL,
                       subject_id UUID NOT NULL,
                       title VARCHAR(150) NOT NULL,
                       exam_date DATE NOT NULL,
                       weight INTEGER NOT NULL DEFAULT 1,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_exams_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_exams_subject
                           FOREIGN KEY (subject_id)
                               REFERENCES subjects(id)
                               ON DELETE CASCADE,

                       CONSTRAINT chk_exams_weight
                           CHECK (weight >= 1 AND weight <= 10)
);