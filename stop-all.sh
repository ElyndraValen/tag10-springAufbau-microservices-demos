#!/bin/bash

echo "🛑 Stopping all Microservices..."
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

if [ -d ".pids" ]; then
    for pidfile in .pids/*.pid; do
        if [ -f "$pidfile" ]; then
            PID=$(cat "$pidfile")
            SERVICE=$(basename "$pidfile" .pid)
            
            if ps -p $PID > /dev/null 2>&1; then
                echo "Stopping $SERVICE (PID: $PID)..."
                kill $PID
                echo -e "${GREEN}✓ $SERVICE stopped${NC}"
            else
                echo -e "${RED}✗ $SERVICE not running${NC}"
            fi
            rm "$pidfile"
        fi
    done
    rmdir .pids 2>/dev/null
else
    echo "No PIDs found. Trying to kill by port..."
    
    # Kill by port
    lsof -ti:8761 | xargs kill -9 2>/dev/null && echo "✓ Stopped Eureka (8761)"
    lsof -ti:8081 | xargs kill -9 2>/dev/null && echo "✓ Stopped User Service (8081)"
    lsof -ti:8082 | xargs kill -9 2>/dev/null && echo "✓ Stopped Order Service (8082)"
    lsof -ti:8080 | xargs kill -9 2>/dev/null && echo "✓ Stopped API Gateway (8080)"
fi

echo ""
echo -e "${GREEN}✅ All services stopped!${NC}"
