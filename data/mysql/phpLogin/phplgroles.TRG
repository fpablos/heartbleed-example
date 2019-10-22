TYPE=TRIGGERS
triggers='CREATE DEFINER=`` TRIGGER prevent_deletion_of_required_roles\n                                                          BEFORE DELETE\n                                                          ON phplgroles\n                                                          FOR EACH ROW\n                                                          BEGIN\n                                                            IF OLD.required = 1 THEN\n                                                              SIGNAL SQLSTATE \'45000\' SET MESSAGE_TEXT = \'Cannot delete required roles\';\n                                                            END IF;\n                                                        END'
sql_modes=0
definers='@'
client_cs_names='latin1'
connection_cl_names='latin1_swedish_ci'
db_cl_names='latin1_swedish_ci'
