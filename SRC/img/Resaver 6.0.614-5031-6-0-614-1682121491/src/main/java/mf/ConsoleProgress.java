/*
 * Copyright 2022 Mark.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mf;

/**
 *
 * @author Mark
 */
final public class ConsoleProgress {

    public ConsoleProgress() {
        reset(null, 1);
    }
    
    public boolean toggleQuiet()
    {
        this.quiet = !this.quiet;
        return this.quiet;
    }
    
    public <T> void reset(String newLabel, java.util.Collection<T> collection) {
        reset(newLabel, collection.size());
    }
    
    public <S,T> void reset(String newLabel, java.util.Map<S,T> map) {
        reset(newLabel, map.size());
    }
    
    public <T> void reset(String newLabel, T[] arr) {
        reset(newLabel, arr.length);
    }
    
    public void reset(String newLabel, int newTotal) {
        assert newTotal >= 0;
        current = 0;
        total = newTotal;
        division = newTotal / 70;
        label = newLabel;        
        timer = Timer.startNew(newLabel);
    }
    
    public <T> boolean inc(T v) {
        inc();
        return true;
    }
    
    public void inc() {
        current += 1;
        
        if (quiet) return;
        
        if (current == 1 && label != null) {
            System.out.print(label);            
        }
        
        if (current == total) {
            System.out.println(timer.getFormattedTime());            
        }
        else if (division > 1 && current % division == 0) {
            System.out.print('.');
            System.out.flush();
        }
    }
    
    private Timer timer = null;
    private int current = 0;
    private String label;
    private int total;
    private int division;
    private boolean quiet;
}
