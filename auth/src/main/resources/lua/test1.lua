
local key = KEYS[1]

local a =  redis.call("GET", key);

if a == 'lisi'

then


        return a

else

        return 0

end

