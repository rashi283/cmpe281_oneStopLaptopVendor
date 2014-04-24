/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package course;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    DBCollection postsCollection;

    public BlogPostDAO(final DB blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public DBObject findByPermalink(String permalink) {

        DBObject post = null;
        // XXX HW 3.2,  Work Here
        DBCursor cursor = postsCollection.find(new BasicDBObject("permalink", permalink));
        while(cursor.hasNext())
        {
        	DBObject record = cursor.next();
        	post = (DBObject) record;
        }	
        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<DBObject> findByDateDescending(int limit) {

        List<DBObject> posts = null;
        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        posts = new ArrayList<DBObject>();
        DBCursor cursor = postsCollection.find().sort(new BasicDBObject("date", -1)).limit(limit);
        while(cursor.hasNext())
        {
        	DBObject record = cursor.next();
        	posts.add(record);
        }	
        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        BasicDBObject post = new BasicDBObject();
        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        ArrayList<String> comments = new ArrayList<String>();
        post.append("title", title)
        	.append("author", username)
        	.append("body", body)
        	.append("permalink", permalink)
        	.append("tags", tags)
        	.append("comments", comments)
        	.append("date", new Date());

        try {
            postsCollection.insert(post);
            return permalink;
        } catch (MongoException.DuplicateKey e) {
            System.out.println("Post already on blog: " + post);
            return null;
        }
    }




   // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments	   
    	
    	BasicDBObject newComment = new BasicDBObject();
        //newComment.append("comments", new BasicDBObject().append("author", name).append("body",body));
        BasicDBObject data = new BasicDBObject("author",name).append("body",body);
        if (email!=null)
        {
            data.append("email",email); 
        }
        newComment.put("$push",new BasicDBObject("comments",data));
        BasicDBObject searchQuery = new BasicDBObject().append("permalink", permalink);
        postsCollection.update(searchQuery, newComment);
        
//        BasicDBObject data = new BasicDBObject();
//        data.append("author",name).append("body",body);
//        if(!email.equals(""))
//        	data.append("email", email);
//        BasicDBList comments = new BasicDBList();
//        comments.add(data);
//        DBObject searchQuery = findByPermalink(permalink);//new BasicDBObject().append("permalink", permalink);
//        postsCollection.update(searchQuery, comments);
        
        //postsCollection.update(searchQuery, new BasicDBObject("$set", new BasicDBObject ("comments", data)));
    	
//        BasicDBObject addComment = new BasicDBObject();
//        if(!email.equals(""))
//        	addComment.append("author", name).append("email", email).append("body", body);
//        else
//        	addComment.append("author", name).append("body", body);
//        DBObject searchQuery = findByPermalink(permalink);
//        postsCollection.update(searchQuery, new BasicDBObject("$set", new BasicDBObject("comments", addComment)));
        //putComment.put(addComment);
        
        //QueryBuilder
        //QueryBuilder build = QueryBuilder.start();
        //postsCollection.update(build.equals(searchQuery), )

    }


}
