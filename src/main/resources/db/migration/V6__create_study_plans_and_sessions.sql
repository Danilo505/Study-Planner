CREATE TABLE study_plans (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             user_id UUID NOT NULL,
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             hours_per_day INTEGER NOT NULL,
                             status VARCHAR(20) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_study_plans_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT chk_study_plans_hours_per_day
                                 CHECK (hours_per_day >= 1 AND hours_per_day <= 12)
);

CREATE TABLE study_sessions (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                study_plan_id UUID NOT NULL,
                                topic_id UUID NOT NULL,
                                planned_date DATE NOT NULL,
                                planned_hours INTEGER NOT NULL,
                                completed BOOLEAN NOT NULL DEFAULT FALSE,
                                completed_at TIMESTAMP,

                                CONSTRAINT fk_study_sessions_plan
                                    FOREIGN KEY (study_plan_id)
                                        REFERENCES study_plans(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_study_sessions_topic
                                    FOREIGN KEY (topic_id)
                                        REFERENCES topics(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT chk_study_sessions_planned_hours
                                    CHECK (planned_hours >= 1)
);