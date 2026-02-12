-- Store
INSERT INTO store (id, name, timezone)
VALUES ('11111111-1111-1111-1111-111111111111', 'Hofer Filiale 1', 'Europe/Vienna');

-- Employees
INSERT INTO employee (
  id, first_name, last_name, weekly_hours_target,
  saturday_only, only_early_shift, only_late_shift,
  store_id, active
)
VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Max',  'Muster',   20, false, false, false,
 '11111111-1111-1111-1111-111111111111', true),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Anna', 'Huber',    18, false, true,  false,
 '11111111-1111-1111-1111-111111111111', true),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Tom',  'Beispiel', 10, true,  false, false,
 '11111111-1111-1111-1111-111111111111', true);